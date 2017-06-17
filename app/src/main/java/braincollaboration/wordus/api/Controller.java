package braincollaboration.wordus.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {

    private static final String BASE_URL = "https://developers.lingvolive.com/";

    public static ABBYYLingvoAPI getApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ABBYYLingvoAPI.class);

    }
}
