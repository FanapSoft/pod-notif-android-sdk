package ir.podspace.notificationpushsample.presenter.verity_otp;

public interface OTPContract {
    interface OTPView {
        void getExpireIn(int expireIn);

        void onSuccess(boolean isResend);

        void onNetworkError(boolean isResend);

        void onError(boolean isResend, String message);
    }

    interface OTPPresenter {
        void getExpireIn();

        void verify(String code);

        void resend();
    }
}
