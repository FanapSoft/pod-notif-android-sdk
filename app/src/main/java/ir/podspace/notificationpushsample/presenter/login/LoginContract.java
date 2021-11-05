package ir.podspace.notificationpushsample.presenter.login;

public interface LoginContract {
    interface LoginView {
        void onSuccess(String message);

        void onNetworkError();

        void onError(String message);
    }

    interface LoginPresenter {
        void handshake(String mobile);
    }
}
