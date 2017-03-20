package braincollaboration.wordus.api;

import braincollaboration.wordus.model.WordResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by braincollaboration on 19/03/2017.
 */

public interface ApiInterface {

    @POST("/api/v1.1/authenticate")
    Call<String> getBearerByToken(@Header("Authorization") String apiToken);

    @GET("/api/v1/Article?")
    Call<WordResponse> getWordMeaning(@Header("Authorization") String bearer, @Query("heading") String heading, @Query("dict") String dictName, @Query("srcLang") String srcLang, @Query("dstLang") String dstLang);

}
