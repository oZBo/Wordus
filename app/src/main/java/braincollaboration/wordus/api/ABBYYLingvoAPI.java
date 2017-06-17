package braincollaboration.wordus.api;

import braincollaboration.wordus.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ABBYYLingvoAPI {

    //@Headers("Authorization: Basic " + Constants.APP_KEY)
    @GET("api/v1.1/authenticate")
    Call<GetBearerToken> getBearerToken();
}
