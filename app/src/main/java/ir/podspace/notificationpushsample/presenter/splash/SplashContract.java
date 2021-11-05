package ir.podspace.notificationpushsample.presenter.splash;

public
interface SplashContract {
    interface SplashView{
        void checkLogin(boolean isLogin);
    }

    interface SplashPresenter{
        void checkLogin();
    }
}
