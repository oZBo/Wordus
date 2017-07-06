package braincollaboration.wordus.api;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import braincollaboration.wordus.utils.Constants;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {
    private static String accessToken = null;
    private static Boolean tokenExpired;
    private static OkHttpClient.Builder okHttpBuilder;
    private static Request accessTokenRequest;
    private static ABBYYLingvoAPI apiInterfaceInstance;

    public static synchronized ABBYYLingvoAPI getInstance() {
        if (apiInterfaceInstance == null) {
            apiInterfaceInstance = getApi();
        }
        return apiInterfaceInstance;
    }

    private static ABBYYLingvoAPI getApi() {
        initializeRetrofitClients();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpBuilder.build())
                .build();

        return retrofit.create(ABBYYLingvoAPI.class);
    }

    /**
     * This method initializes the retrofit clients
     * a) One for the initial authentication end point
     * b) Other for other service requests
     */
    private static void initializeRetrofitClients() {
        okHttpBuilder = new OkHttpClient().newBuilder();
        okHttpBuilder.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                // Nothing to add to intercepted request if:
                // a) Authorization value is empty because user is not logged in yet
                // b) There is already a header with updated Authorization value
                if (authorizationTokenIsEmpty() || alreadyHasAuthorizationHeader(originalRequest)) {
                    return chain.proceed(originalRequest);
                }

                // Add authorization header with updated authorization value to intercepted request
                Request authorisedRequest = originalRequest.newBuilder()
                        .header("Authorization: Bearer ", accessToken)
                        .build();
                return chain.proceed(authorisedRequest);
            }
        });
        okHttpBuilder.authenticator(new Authenticator() {
            @Nullable
            @Override
            public Request authenticate(Route route, okhttp3.Response response) throws IOException {
                // Refresh your access_token using a synchronous api request
                ABBYYLingvoAPI abbyyLingvoAPI = getApi();

                Call<ResponseBody> myCall = abbyyLingvoAPI.getBasicToken();
                Response<ResponseBody> response1 = null;
                try {
                    response1 = myCall.execute();
                    if (response1.isSuccessful()) {
                        accessToken = response1.body().string();
                        Log.e(Constants.LOG_TAG, "basic response is success, got accessToken");
                    } else {
                        Log.e(Constants.LOG_TAG, "basic response isn't successful");
                    }
                } catch (IOException e) {
                    Log.e(Constants.LOG_TAG, "basic response isn't successful");
                }

                // Add new header to rejected request and retry it
                accessTokenRequest = response.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .build();

                return accessTokenRequest;
            }
        });

//        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//        OkHttpClient clientNormal;
//        OkHttpClient clientAuthenticated;
//        builder.interceptors().add(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//                Request.Builder builder = originalRequest.newBuilder().header("Authorization: Bearer ", accessToken).
//                        method(originalRequest.method(), originalRequest.body());
//                okhttp3.Response response = chain.proceed(builder.build());
//                /**
//                 implies that the token has expired
//                 or was never initialized
//                 */
//                if (response.code() == 401) {
//                    tokenExpired = true;
//                    logger.info("Token Expired");
//                    getAuthenticationToken();
//                    builder = originalRequest.newBuilder().header("Authorization: Bearer ", accessToken).
//                            method(originalRequest.method(), originalRequest.body());
//                    response = chain.proceed(builder.build());
//                }
//                return response;
//            }
//        });
//        clientAuthenticated = builder.build();
//        retrofitAuthenticated = new Retrofit.Builder().client(clientAuthenticated)
//                .baseUrl(API_ENDPOINT)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        OkHttpClient.Builder builder1 = new OkHttpClient().newBuilder();
//        builder1.authenticator(new Authenticator() {
//            @Override
//            public Request authenticate(Route route, okhttp3.Response response) throws IOException {
//                String authentication = Credentials.basic(CLIENT_ID, CLIENT_SECRET);
//                Request.Builder builder = response.request().newBuilder().addHeader("Authorization", authentication);
//                return builder.build();
//            }
//        });
//        clientNormal = builder1.build();
//        retrofit = new Retrofit.Builder().client(clientNormal).
//                baseUrl(API_ENDPOINT).
//                addConverterFactory(GsonConverterFactory.create()).build();
//    }
//
//    /**
//     * Is invoked only when the access token is required
//     * Or it expires
//     */
//    private void getAuthenticationToken() {
//        ABBYYLingvoAPI abbyyLingvoAPI = getApi();
//
//        Call<ResponseBody> myCall = abbyyLingvoAPI.getBasicToken();
//        Response<ResponseBody> response = null;
//        try {
//            response = myCall.execute();
//            if (response.isSuccessful()) {
//                accessToken = response.body().string();
//                Log.e(Constants.LOG_TAG, "basic response is success");
//            } else {
//                Log.e(Constants.LOG_TAG, "basic response isn't successful");
//            }
//        } catch (IOException e) {
//            Log.e(Constants.LOG_TAG, "basic response isn't successful");
//        }
    }

    private static boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        return originalRequest == accessTokenRequest;
    }

    private static boolean authorizationTokenIsEmpty() {
        return accessToken == null;
    }
}
