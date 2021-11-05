package ir.podspace.notificationpushsample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import ir.podspace.notificationpushsample.pc.Constant;
import ir.podspace.notificationpushsample.presenter.splash.SplashContract;
import ir.podspace.notificationpushsample.presenter.splash.SplashPresenterImpl;


public class SplashActivity extends AppCompatActivity implements SplashContract.SplashView {
    private SplashContract.SplashPresenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        presenter = new SplashPresenterImpl(this, this);
        presenter.checkLogin();
    }

    @Override
    public void checkLogin(boolean isLogin) {
        Intent intent;

        if (isLogin) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }, Constant.SPLASH_TIME);
    }
}