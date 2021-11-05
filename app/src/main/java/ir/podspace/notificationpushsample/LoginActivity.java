package ir.podspace.notificationpushsample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import ir.podspace.notificationpushsample.presenter.login.LoginContract;
import ir.podspace.notificationpushsample.presenter.login.LoginPresenterImpl;
import ir.podspace.notificationpushsample.view.loading_button.KS_LoadingButton;


public class LoginActivity extends AppCompatActivity implements LoginContract.LoginView {
    private LoginContract.LoginPresenter presenter;

    TextInputEditText txtMobile;
    KS_LoadingButton btnSubmit;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenterImpl(this, this);
        txtMobile = findViewById(R.id.txtMobile);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        String mobile = txtMobile.getText().toString();

        if (mobile.matches("")) {
            Toast.makeText(LoginActivity.this, "شماره موبایل را وارد نمائید", Toast.LENGTH_SHORT).show();
            txtMobile.requestFocus();
            return;
        }

        btnSubmit.startLoading();
        presenter.handshake(mobile);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(LoginActivity.this, "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

        Intent loginIntent = new Intent(LoginActivity.this, OtpActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(loginIntent);

        finish();
    }

    @Override
    public void onNetworkError() {
        btnSubmit.stopLoading();
        Toast.makeText(LoginActivity.this, "عدم اتصال به اینترنت", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        btnSubmit.stopLoading();
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}