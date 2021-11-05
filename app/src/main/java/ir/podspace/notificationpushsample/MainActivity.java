package ir.podspace.notificationpushsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import ir.fanap.sdk_notif.presenter.ResponseListener;
import ir.fanap.sdk_notif.view.PushSdk;
import ir.podspace.notificationpushsample.model.HawkHelper;
import ir.podspace.notificationpushsample.pc.Constant;
import ir.podspace.notificationpushsample.presenter.logout.LogoutContract;
import ir.podspace.notificationpushsample.presenter.logout.LogoutPresenterImpl;
import ir.podspace.notificationpushsample.view.loading_button.KS_LoadingButton;

public class MainActivity extends AppCompatActivity implements LogoutContract.LogoutView {
    private LogoutContract.LogoutPresenter presenter;
    private boolean isRegister = false;

    TextView lblResponse;
    TextInputEditText txtAppId;
    KS_LoadingButton btnRegister;
    KS_LoadingButton btnExit;
    PushSdk pushSdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblResponse = findViewById(R.id.lblResponse);
        txtAppId = findViewById(R.id.txtAppId);
        btnRegister = findViewById(R.id.btnSubmit);
        btnExit = findViewById(R.id.btnExit);

        presenter = new LogoutPresenterImpl(this, this);

        btnRegister.setOnClickListener(view -> {
            String appId = txtAppId.getText().toString();
            String token = (String) HawkHelper.getData(MainActivity.this, Constant._ACCESS_TOKEN);

            if (token == null) {
                Toast.makeText(MainActivity.this, "توکن خود را انتخاب نمائید", Toast.LENGTH_LONG).show();
                return;
            }

            if (appId.matches("")) {
                Toast.makeText(MainActivity.this, "شناسه اپلیکیشن را وارد نمائید", Toast.LENGTH_LONG).show();
                return;
            }

            btnRegister.startLoading();

            pushSdk = new PushSdk.Builder()
                    .setContext(MainActivity.this)
                    .setApiToken(token)
                    .setAppId(appId)
                    .setHandleNotification(false)
                    .setResponseListener(new ResponseListener() {
                        @Override
                        public void onSubscribe(JSONObject jsonObject) {
                            btnRegister.stopLoading();
                            lblResponse.setText(jsonObject.optString("fcmToken"));
                            lblResponse.setTextColor(Color.GREEN);
                        }

                        @Override
                        public void onUnsubscribe() {
                            presenter.logout();
                        }

                        @Override
                        public void onError(Exception e) {
                            btnRegister.stopLoading();
                            lblResponse.setText(e.getMessage());
                            lblResponse.setTextColor(Color.RED);
                        }
                    })
                    .build();
        });

        btnExit.setOnClickListener(v -> {
            btnExit.startLoading();
            if (pushSdk != null){
                pushSdk.unsubscribe();
            }else {
                navToLogin();
            }
        });
    }

    private void navToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(loginIntent);
    }

    @Override
    public void logout() {
        btnExit.stopLoading();

        Toast.makeText(MainActivity.this, "اشتراک کاربر با موفقیت غیر فعال شد", Toast.LENGTH_LONG).show();
        navToLogin();
    }

    @Override
    public void onNetworkError() {
        btnExit.stopLoading();
        Toast.makeText(MainActivity.this, "عدم اتصال به اینترنت", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String message) {
        btnExit.stopLoading();
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
}