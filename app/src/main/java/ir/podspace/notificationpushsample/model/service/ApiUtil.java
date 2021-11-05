package ir.podspace.notificationpushsample.model.service;

import static ir.podspace.notificationpushsample.pc.Constant.BASE_URL;
import static ir.podspace.notificationpushsample.pc.Constant.SPACE_URL;

import android.content.Context;

/**
 * Created by Sadegh-Pc on 3/7/2018.
 */

public class ApiUtil {


    public static ApiService getApiService(Context context) {
        return RetrofitClient.getClient(context, BASE_URL).create(ApiService.class);
    }

    public static ApiService getApiServiceWithToken(Context context) {
        return RetrofitClient.getClientWithToken(context, BASE_URL).create(ApiService.class);
    }

    public static ApiService getSpaceService(Context context) {
        return RetrofitClient.getClasorClient(context, SPACE_URL).create(ApiService.class);
    }
}
