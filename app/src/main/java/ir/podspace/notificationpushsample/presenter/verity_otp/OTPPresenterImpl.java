package ir.podspace.notificationpushsample.presenter.verity_otp;

import android.content.Context;

import java.io.IOException;

import ir.podspace.notificationpushsample.model.HawkHelper;
import ir.podspace.notificationpushsample.model.service.ApiService;
import ir.podspace.notificationpushsample.model.service.ApiUtil;
import ir.podspace.notificationpushsample.pc.Constant;
import ir.podspace.notificationpushsample.pc.authorize.AuthorizeResponseModel;
import ir.podspace.notificationpushsample.pc.verify.VerifyResponseModel;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OTPPresenterImpl implements OTPContract.OTPPresenter {
    private Context context;
    private OTPContract.OTPView view;
    private Subscription subscription;
    private ApiService apiService;

    public OTPPresenterImpl(Context context, OTPContract.OTPView view) {
        this.context = context;
        this.view = view;
        this.apiService = ApiUtil.getApiService(context);
    }

    @Override
    public void getExpireIn() {
        try {
            int expireIn = Integer.parseInt(HawkHelper.getData(context, Constant._EXPIRE_IN).toString());
            view.getExpireIn(expireIn);
        } catch (Exception ex) {
            view.onError(false, ex.getMessage());
        }
    }

    @Override
    public void verify(String code) {
        try {
            String keyId = HawkHelper.getData(context, Constant._KEY_ID).toString();
            String mobile = HawkHelper.getData(context, Constant._MOBILE).toString();

            subscription = apiService.verify(keyId, mobile, code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new VerifyPresenter());
        } catch (Exception ex) {
            view.onError(false, ex.getMessage());
        }
    }

    @Override
    public void resend() {
        try {
            String keyId = HawkHelper.getData(context, Constant._KEY_ID).toString();
            String mobile = HawkHelper.getData(context, Constant._MOBILE).toString();

            subscription = apiService.authorize(keyId, mobile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new AuthorizeResponse());
        } catch (Exception ex) {
            view.onError(true, ex.getMessage());
        }
    }

    private class VerifyPresenter extends Subscriber<VerifyResponseModel> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            handleThrowable(false, e);
        }

        @Override
        public void onNext(VerifyResponseModel response) {
            try {
                if (response.getStatus() == Constant.SUCCESS) {
                    HawkHelper.setData(context, Constant._ACCESS_TOKEN, response.getResult().getAccess_token());
                    HawkHelper.setData(context, Constant._REFRESH_TOKEN, response.getResult().getRefresh_token());
                    HawkHelper.setData(context, Constant._DEVICE_ID, response.getResult().getDevice_uid());
                    HawkHelper.setData(context, Constant._SCOPE, response.getResult().getScope());
                    HawkHelper.setData(context, Constant._TOKEN_TYPE, response.getResult().getToken_type());
                    HawkHelper.setData(context, Constant._EXPIRE_IN, response.getResult().getExpires_in());

                    view.onSuccess(false);
                } else {
                    view.onError(false, response.getMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                view.onError(false, ex.getMessage());
            }
        }
    }

    private class AuthorizeResponse extends Subscriber<AuthorizeResponseModel> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            handleThrowable(true, e);
        }

        @Override
        public void onNext(AuthorizeResponseModel responseModel) {
            try {
                if (responseModel.getStatus() == Constant.SUCCESS) {
                    HawkHelper.setData(context, Constant._USER_ID, responseModel.getResult().getUser_id());
                    HawkHelper.setData(context, Constant._EXPIRE_IN, responseModel.getResult().getExpires_in());
                    HawkHelper.setData(context, Constant._MOBILE, responseModel.getResult().getIdentity());

                    view.onSuccess(true);
                } else {
                    view.onError(true, responseModel.getMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                view.onError(true, ex.getMessage());
            }
        }
    }


    private void handleThrowable(boolean isResend, Throwable e) {
        if (e instanceof IOException) {
            view.onNetworkError(isResend);
        } else if (e instanceof HttpException) {
            try {
                HttpException httpException = ((HttpException) e);


            } catch (Exception ex) {
                view.onError(isResend, ex.getMessage());
            }
        }
    }
}
