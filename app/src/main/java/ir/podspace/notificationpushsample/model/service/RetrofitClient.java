package ir.podspace.notificationpushsample.model.service;

import android.content.Context;

import ir.podspace.notificationpushsample.BuildConfig;
import ir.podspace.notificationpushsample.pc.Constant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sadegh-Pc on 3/7/2018.
 * <p>
 * Config retrofit and return new instance
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context, String baseUrl) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(null)
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static Retrofit getClientWithToken(Context context, String baseUrl) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HeaderInterceptor headerInterceptor = new HeaderInterceptor(context, Constant.Project.FARAPOD);
        TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(context);

        if (BuildConfig.DEBUG) {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(null)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .authenticator(tokenAuthenticator)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static Retrofit getClasorClient(Context context, String baseUrl) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HeaderInterceptor headerInterceptor = new HeaderInterceptor(context, Constant.Project.CLASOR);
        TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(context);

        if (BuildConfig.DEBUG) {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(null)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .authenticator(tokenAuthenticator)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }
}
