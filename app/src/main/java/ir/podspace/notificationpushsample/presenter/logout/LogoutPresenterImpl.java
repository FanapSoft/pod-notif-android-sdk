package ir.podspace.notificationpushsample.presenter.logout;

import android.content.Context;

import java.io.IOException;

import ir.podspace.notificationpushsample.model.HawkHelper;
import ir.podspace.notificationpushsample.model.service.ApiService;
import ir.podspace.notificationpushsample.model.service.ApiUtil;
import ir.podspace.notificationpushsample.pc.Constant;
import ir.podspace.notificationpushsample.pc.verify.VerifyResponseModel;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LogoutPresenterImpl implements LogoutContract.LogoutPresenter {
    private Context context;
    private LogoutContract.LogoutView view;
    private Subscription subscription;
    private ApiService apiService;

    public LogoutPresenterImpl(Context context, LogoutContract.LogoutView view) {
        this.context = context;
        this.view = view;
        this.apiService = ApiUtil.getApiService(context);
    }

    @Override
    public void logout() {
        try {
            String token = HawkHelper.getData(context, Constant._ACCESS_TOKEN).toString();

            subscription = apiService.revokeToken(token, "access_token")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new AuthorizeResponse());
        } catch (Exception ex) {
            view.onError(ex.getMessage());
        }
    }

    private class AuthorizeResponse extends Subscriber<VerifyResponseModel> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            handleThrowable(e);
        }

        @Override
        public void onNext(VerifyResponseModel responseModel) {
            try {
                if (responseModel.getStatus() == Constant.SUCCESS) {
                    HawkHelper.clearData(context);

                    view.logout();
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
