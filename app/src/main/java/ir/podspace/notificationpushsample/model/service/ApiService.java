package ir.podspace.notificationpushsample.model.service;


import static ir.podspace.notificationpushsample.pc.Constant.AUTHORIZE;
import static ir.podspace.notificationpushsample.pc.Constant.HANDSHAKE;
import static ir.podspace.notificationpushsample.pc.Constant.REFRESH_TOKEN;
import static ir.podspace.notificationpushsample.pc.Constant.REVOKE_TOKEN;
import static ir.podspace.notificationpushsample.pc.Constant.VERIFY;

import ir.podspace.notificationpushsample.pc.authorize.AuthorizeResponseModel;
import ir.podspace.notificationpushsample.pc.handshake.HandshakeResponseModel;
import ir.podspace.notificationpushsample.pc.verify.VerifyResponseModel;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

;

/**
 * Created by Sadegh-Pc on 3/7/2018.
 */

public interface ApiService {
    @Headers({"Accept: application/json"})
    @POST(HANDSHAKE)
    Observable<HandshakeResponseModel> handshake(@Query("deviceName") String deviceName,
                                                 @Query("deviceOs") String deviceOs,
                                                 @Query("deviceOsVersion") String deviceOsVersion,
                                                 @Query("deviceType") String deviceType,
                                                 @Query("deviceUID") String deviceUID);

    @Headers({"Accept: application/json"})
    @POST(AUTHORIZE)
    Observable<AuthorizeResponseModel> authorize(@Header("keyId") String keyId,
                                                 @Query("identity") String identity);

    @Headers({"Accept: application/json"})
    @POST(VERIFY)
    Observable<VerifyResponseModel> verify(@Header("keyId") String keyId,
                                           @Query("identity") String identity,
                                           @Query("otp") String otp);

    @Headers({"Accept: application/json"})
    @GET(REFRESH_TOKEN)
    Call<VerifyResponseModel> refreshToken(@Query("refreshToken") String refreshToken);

    @Headers({"Accept: application/json"})
    @DELETE(REVOKE_TOKEN)
    Observable<VerifyResponseModel> revokeToken(@Query("token") String identity,
                                                @Query("tokenType") String otp);
}
