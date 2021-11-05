package ir.podspace.notificationpushsample.model.service;

import static ir.podspace.notificationpushsample.model.HawkHelper.getData;
import static ir.podspace.notificationpushsample.model.HawkHelper.setData;

import android.content.Context;

import java.io.IOException;

import ir.podspace.notificationpushsample.pc.Constant;
import ir.podspace.notificationpushsample.pc.verify.VerifyResponseModel;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

public class TokenAuthenticator implements Authenticator {
    private Context context;
    private ApiService apiService;

    public TokenAuthenticator(Context context) {
        this.context = context;
        this.apiService = ApiUtil.getApiService(context);
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        String refreshToken = (String) getData(context, Constant._REFRESH_TOKEN);

        Call<VerifyResponseModel> verifyResponseModelCall = apiService.refreshToken(refreshToken);

        VerifyResponseModel responseModel = verifyResponseModelCall.execute().body();

        assert responseModel != null;
        if (responseModel.getStatus() == Constant.SUCCESS) {

            setData(context,Constant._ACCESS_TOKEN,responseModel.getResult().getAccess_token());
            setData(context,Constant._REFRESH_TOKEN,responseModel.getResult().getRefresh_token());

            return response.request().newBuilder()
//                    .header("_token_", responseModel.getResult().getAccess_token())
//                    .header("_token_issuer_", Constant._TOKEN_ISSUER)
                    .build();
        }

        return null;
    }
}
