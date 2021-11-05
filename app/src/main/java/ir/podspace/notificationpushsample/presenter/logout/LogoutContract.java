package ir.podspace.notificationpushsample.presenter.logout;

public interface LogoutContract {
    interface LogoutView{
        void logout();
        void onNetworkError();
        void onError(String message);
    }

    interface LogoutPresenter{
        void logout();
    }
}
