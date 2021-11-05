package ir.podspace.notificationpushsample.presenter.splash;

import android.content.Context;

import ir.podspace.notificationpushsample.model.HawkHelper;
import ir.podspace.notificationpushsample.pc.Constant;

public class SplashPresenterImpl implements SplashContract.SplashPresenter {
    private Context context;
    private SplashContract.SplashView view;

    public SplashPresenterImpl(Context context, SplashContract.SplashView view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void checkLogin() {
        String token = (String) HawkHelper.getData(context, Constant._ACCESS_TOKEN);

        if (token == null || token.matches("") || token.isEmpty()) {
            view.checkLogin(false);
        } else {
            view.checkLogin(true);
        }
    }
}
