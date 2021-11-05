package ir.podspace.notificationpushsample.pc;

public class Constant {
    public static final long SPLASH_TIME = 2000;
    public static long BACK_PRESSED = 2000;

    public static final String _ID = "ID";
    public static final String _TOKEN_ISSUER = "1";
    public static final String _TOKEN_TYPE = "TOKEN_TYPE";
    public static final String _ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String _REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String _BEARER_TOKEN = "Bearer ";
    public static final String _MOBILE = "MOBILE";
    public static final String _CODE = "CODE";
    public static final String _TITLE = "TITLE";
    public static final String _DESCRIPTION = "DESCRIPTION";
    public static final String _KEY_ID = "KEY_ID";
    public static final String _EXPIRE_IN = "EXPIRE_IN";
    public static final String _USER_ID = "USER_ID";

    public static final String _DEVICE_ID = "DEVICE_ID";
    public static final String _DEVICE_OS = "ANDROID";
    public static String _DEVICE_TYPE = "MOBILE_PHONE";
    public static String _SCOPE = "SCOPE";
    public static int SUCCESS = 200;


    public static final String BASE_URL = "http://podspace.pod.ir/api/";
    public static final String SPACE_URL = "https://sandbox.podspace.ir/api/";


    public static final String HANDSHAKE = "oauth2/otp/handshake";
    public static final String AUTHORIZE = "oauth2/otp/authorize";
    public static final String VERIFY = "oauth2/otp/verify";
    public static final String REFRESH_TOKEN = "oauth2/refresh";
    public static final String REVOKE_TOKEN = "oauth2/token";


    public static enum PresentationType {
        ONLINE(3),
        OFFLINE(4),
        ONLINE_OFFLINE(5);

        private int value;

        PresentationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static String findByValue(int value) {
            for (PresentationType presentationType : values()) {
                if (presentationType.getValue() == value) {
                    return presentationType.name();
                }
            }

            return null;
        }
    }

    public static enum Project{
        SSO,
        FARAPOD,
        CLASOR,
    }
}
