package ir.podspace.notificationpushsample;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.podspace.notificationpushsample.presenter.verity_otp.OTPContract;
import ir.podspace.notificationpushsample.presenter.verity_otp.OTPPresenterImpl;
import ir.podspace.notificationpushsample.view.loading_button.KS_LoadingButton;


public class OtpActivity extends AppCompatActivity implements OTPContract.OTPView {
    private String mobile = "";
    private CountDownTimer timer;

    private OTPContract.OTPPresenter presenter;

    TextInputEditText txtOtp;
    KS_LoadingButton btnSubmit;
    KS_LoadingButton btnResend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        txtOtp = findViewById(R.id.txtOtp);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnResend = findViewById(R.id.btnResend);

        presenter = new OTPPresenterImpl(this, this);
        presenter.getExpireIn();

        initView();
    }

    private void initView() {
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend();
            }
        });
    }

    private void submit() {
        String code = txtOtp.getText().toString();

        if (code.matches("")) {
            Toast.makeText(OtpActivity.this, "کد را وارد نمائید", Toast.LENGTH_LONG).show();
            return;
        }

        btnSubmit.startLoading();
        presenter.verify(code);
    }

    private void resend() {
        btnResend.startLoading();
        btnResend.setEnabled(false);
        presenter.resend();
    }


    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // ...
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // `sms` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                    StringBuilder code = new StringBuilder();

                    Pattern p = Pattern.compile("-?\\d+");
                    Matcher m = p.matcher(message);
                    while (m.find()) {
                        code.append(m.group());
                        break;
                    }

                    txtOtp.setText(code.toString());
                    btnSubmit.performClick();
                }
                break;
        }
    }

    @Override
    public void getExpireIn(int expireIn) {
        long millisInFuture = expireIn * 1000;
        timer = new CountDownTimer(millisInFuture, 1000) {

            @Override
            public void onTick(long time) {
                long minute = time / (60 * 1000) % 60;
                long seconds = time / 1000 % 60;

                btnResend.setEnabled(false);
                btnResend.setmText(minute + ":" + seconds);
            }

            @Override
            public void onFinish() {
                btnResend.setEnabled(true);
                btnResend.setmText("ارسال مجدد");
            }
        }.start();

        SmsRetriever.getClient(OtpActivity.this).startSmsUserConsent(null);
    }

    @Override
    public void onSuccess(boolean isResend) {
        Toast.makeText(OtpActivity.this, "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

        if (isResend) {
            btnResend.stopLoading();
            btnResend.setEnabled(false);
            presenter.getExpireIn();
        } else {
            Intent loginIntent = new Intent(OtpActivity.this, MainActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(loginIntent);
        }
    }

    @Override
    public void onNetworkError(boolean isResend) {
        if (isResend) {
            btnResend.stopLoading();
            btnResend.setEnabled(true);
        } else {
            btnSubmit.stopLoading();
        }

        Toast.makeText(OtpActivity.this, "عدم اتصال به اینترنت", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(boolean isResend, String message) {
        if (isResend) {
            btnResend.stopLoading();
            btnResend.setEnabled(true);
        } else {
            btnSubmit.stopLoading();
        }

        Toast.makeText(OtpActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}