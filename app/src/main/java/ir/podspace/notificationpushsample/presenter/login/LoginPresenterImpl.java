package ir.podspace.notificationpushsample.presenter.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.io.IOException;

import ir.podspace.notificationpushsample.model.HawkHelper;
import ir.podspace.notificationpushsample.model.service.ApiService;
import ir.podspace.notificationpushsample.model.service.ApiUtil;
import ir.podspace.notificationpushsample.pc.Constant;
import ir.podspace.notificationpushsample.pc.authorize.AuthorizeResponseModel;
import ir.podspace.notificationpushsample.pc.handshake.HandshakeResponseModel;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginContract.LoginPresenter {
    private Context context;
    private LoginContract.LoginView view;
    private Subscription subscription;
    private ApiService apiService;
    private String mobile;

    public LoginPresenterImpl(Context context, LoginContract.LoginView view) {
        this.context = context;
        this.view = view;
        this.apiService = ApiUtil.getApiService(context);
    }

    @SuppressLint("HardwareIds")
    @Override
    public void handshake(String mobile) {
        try {
            this.mobile = mobile;
            String deviceName = Build.MODEL;
            String osVersion = String.valueOf(Build.VERSION.SDK_INT);
            String uid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            subscription = apiService.handshake(deviceName,
                    Constant._DEVICE_OS, osVersion, Constant._DEVICE_TYPE, uid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HandshakeResponse());
        } catch (Exception ex) {
            view.onError(ex.getMessage());
        }
    }

    public void authorize() {
        try {
            String keyId = HawkHelper.getData(context, Constant._KEY_ID).toString();

            subscription = apiService.authorize(keyId, mobile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new AuthorizeResponse());
        } catch (Exception ex) {
            view.onError(ex.getMessage());
        }
    }

    private class HandshakeResponse extends Subscriber<HandshakeResponseModel> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            handleThrowable(e);
        }

        @Override
        public void onNext(HandshakeResponseModel responseModel) {
            try {
                if (responseModel.getStatus() == Constant.SUCCESS) {
                    HawkHelper.setData(context, Constant._KEY_ID, responseModel.getResult().getKeyId());
                    HawkHelper.setData(context, Constant._EXPIRE_IN, responseModel.getResult().getExpiresIn());

                    authorize();
                } else {
                    view.onError(responseModel.getMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                view.onError(ex.getMessage());
            }
        }
    }

    private class AuthorizeResponse extends Subscriber<AuthorizeResponseModel> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            handleThrowable(e);
        }

        @Override
        public void onNext(AuthorizeResponseModel responseModel) {
            try {
                if (responseModel.getStatus() == Constant.SUCCESS) {
                    HawkHelper.setData(context, Constant._USER_ID, responseModel.getResult().getUser_id());
                    HawkHelper.setData(context, Constant._EXPIRE_IN, responseModel.getResult().getExpires_in());
                    HawkHelper.setData(context, Constant._MOBILE, responseModel.getResult().getIdentity());

                    view.onSuccess("");
                } else {
                    view.onError(responseModel.getMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                view.onError(ex.getMessage());
            }
        }
    }

    private void handleThrowable(Throwable e) {
        if (e instanceof IOException) {
            view.onNetworkError();
        } else if (e instanceof HttpException) {
            try {
                HttpException httpException = ((HttpException) e);


            } catch (Exception ex) {
                view.onError(ex.getMessage());
            }
        }
    }
}
