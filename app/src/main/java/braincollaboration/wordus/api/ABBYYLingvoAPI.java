package braincollaboration.wordus.api;

import braincollaboration.wordus.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ABBYYLingvoAPI {

    @Headers("Authorization: Basic " + Constants.APP_KEY)
    @POST("api/v1.1/authenticate")
    Call<ResponseBody> getBasicToken();

    @GET("api/v1/Search")
    Call<ResponseBody> getWordMeaning (@Query("text") String word, @Query("srcLang") int srcLang, @Query("dstLang") int dstLang, @Query("searchZone") int searchZone, @Query("startIndex") int startIndex, @Query("pageSize") int pageSize);
}
