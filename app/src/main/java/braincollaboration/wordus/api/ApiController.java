package braincollaboration.wordus.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

public class ApiController {

    private static String accessToken;
    private static Request accessTokenRequest;
    private static final ABBYYLingvoAPI apiInterfaceInstance = getApi();

    public static ABBYYLingvoAPI getInstance() {
        return apiInterfaceInstance;
    }

    private ApiController() {
    }

    private static ABBYYLingvoAPI getApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(initializeRetrofitClients())
                .build();

        return retrofit.create(ABBYYLingvoAPI.class);
    }

    /**
     * This method initializes the retrofit clients
     * a) One for the initial authentication end point
     * b) Other for other service requests
     */
    private static OkHttpClient initializeRetrofitClients() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient().newBuilder();
        okHttpBuilder.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();

                // Nothing to add to intercepted request if:
                // a) Authorization value is empty because user is not logged in yet
                // b) There is already a header with updated Authorization value
                if (authorizationTokenIsEmpty() || alreadyHasAuthorizationHeader(originalRequest)) {
                    return chain.proceed(originalRequest);
                }

                // Add authorization header with updated authorization value to intercepted request
                Request authorisedRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + accessToken)
                        .build();
                return chain.proceed(authorisedRequest);
            }
        });
        okHttpBuilder.authenticator(new Authenticator() {
            @Nullable
            @Override
            public Request authenticate(@NonNull Route route, @NonNull okhttp3.Response response) throws IOException {
                // Refresh your access_token using a synchronous api request
                ABBYYLingvoAPI abbyyLingvoAPI = getApi();

                Call<ResponseBody> myCall = abbyyLingvoAPI.getBasicToken();
                Response<ResponseBody> response1;
                try {
                    response1 = myCall.execute();
                    if (response1.isSuccessful()) {
                        accessToken = response1.body().string();
                        Log.d(Constants.LOG_TAG, "basic response is success, got accessToken");
                    } else {
                        Log.e(Constants.LOG_TAG, "basic response isn't successful, response code is: " + response1.code());
                    }
                } catch (IOException e) {
                    Log.e(Constants.LOG_TAG, "basic response isn't successful cause error: " + e.toString());
                }

                // Add new header to rejected request and retry it
                accessTokenRequest = response.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .build();

                return accessTokenRequest;
            }
        });

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okHttpBuilder.sslSocketFactory(sslSocketFactory);
            okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        return originalRequest == accessTokenRequest;
    }

    private static boolean authorizationTokenIsEmpty() {
        return accessToken == null;
    }
}
