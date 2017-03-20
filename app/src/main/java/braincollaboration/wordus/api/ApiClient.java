package braincollaboration.wordus.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.model.WordResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by braincollaboration on 19/03/2017.
 */
public class ApiClient {

    private static final String API_KEY = "Basic NmI1OWY1MjYtYWNiYy00OTQyLTkxMTAtMWRmZWY3ZTZkMDlmOjViOWYyMjExYWZlNTQ2NzBhODQ5MWMwMzRlZDExMjQ5";
    private static final String BASE_URL = "https://developers.lingvolive.com";
    private static final ApiInterface apiService = getClient().create(ApiInterface.class);
    private static Retrofit retrofit = null;
    private static Gson gson = null;
    private static String bearerToken;

    private static Retrofit getClient() {

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



    private static void getBearerTokenCall(final String word){
        Call<String> call = apiService.getBearerByToken(API_KEY);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                bearerToken = response.body();
                getWordMeaning(word);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    //TODO clean up calls queue.
    public static Word getWordMeaning(final String wordToFind){
        final Word word = new Word();
        if(bearerToken == null){
            getBearerTokenCall(wordToFind);
            return null;
        }
        Call<WordResponse> call = apiService.getWordMeaning("Bearer "+bearerToken+"=", "аврал", "Explanatory (Ru-Ru)", "1049", "1049");
        call.enqueue(new Callback<WordResponse>() {
            @Override
            public void onResponse(Call<WordResponse> call, Response<WordResponse> response) {
                if(response.code() == 401){
                    getBearerTokenCall(wordToFind);
                }else if(response.code() == 200){
                    WordResponse wordResponse = response.body();
                }
            }

            @Override
            public void onFailure(Call<WordResponse> call, Throwable t) {

            }
        });
        return null;
    }

}
