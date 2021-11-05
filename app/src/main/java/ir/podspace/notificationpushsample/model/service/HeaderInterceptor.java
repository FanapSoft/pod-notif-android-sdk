package ir.podspace.notificationpushsample.model.service;

import android.content.Context;


import java.io.IOException;

import ir.podspace.notificationpushsample.model.HawkHelper;
import ir.podspace.notificationpushsample.pc.Constant;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private Context context;
    private Constant.Project project;

    public HeaderInterceptor(Context context, Constant.Project project) {
        this.context = context;
        this.project = project;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = (String) HawkHelper.getData(context, Constant._ACCESS_TOKEN);

        Request.Builder requestBuilder = chain.request()
                .newBuilder();

        switch (project) {
            case SSO:
                break;
            case FARAPOD:
                requestBuilder.addHeader("_token_", token);
                requestBuilder.addHeader("_token_issuer_", Constant._TOKEN_ISSUER);
                break;
            case CLASOR:
                requestBuilder.addHeader("Authorization", Constant._BEARER_TOKEN + token);
                break;
        }


        Request request = requestBuilder.build();
        Response response = chain.proceed(request);
        return response;
    }
}
