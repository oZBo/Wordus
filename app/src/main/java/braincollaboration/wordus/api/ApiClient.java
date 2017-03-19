package braincollaboration.wordus.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by braincollaboration on 19/03/2017.
 */
public class ApiClient {

    private static final String BASE_URL = "https://developers.lingvolive.com/";
    private static Retrofit retrofit = null;
    private static Gson gson = null;

    public static Retrofit getClient() {

        if (gson == null) {
            gson = new GsonBuilder()
                    .setLenient()
                    .create();
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
