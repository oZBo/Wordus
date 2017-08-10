package braincollaboration.wordus.manager;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import braincollaboration.wordus.api.ABBYYLingvoAPI;
import braincollaboration.wordus.api.ApiController;
import braincollaboration.wordus.api.JsonResponseNodeTypeDecryption;
import braincollaboration.wordus.background.BackgroundManager;
import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.background.IBackgroundTask;
import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitManager {

    private final static int DICTIONARY_ID = 1049;
    private final static int SEARCH_ZONE = 1;
    private final static int START_INDEX = 0;
    private final static int PAGE_SIZE = 3;

    private RetrofitManager() {
    }

    private static final RetrofitManager ourInstance = new RetrofitManager();

    public static RetrofitManager getInstance() {
        return ourInstance;
    }

    public void searchWordDescription(final Word innerWord, final DefaultBackgroundCallback<Word> callback) {
        ABBYYLingvoAPI abbyyLingvoAPI = ApiController.getInstance();
        final Call<ResponseBody> myCall = abbyyLingvoAPI.getWordMeaning(innerWord.getWordName(), DICTIONARY_ID, DICTIONARY_ID, SEARCH_ZONE, START_INDEX, PAGE_SIZE);
        BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Word>() {
            @Override
            public Word execute() {
                Word outWord = null;
                try {
                    Response<ResponseBody> rb = myCall.execute();
                    int code = rb.code();
                    if (code == 200) {
                        Log.d(Constants.LOG_TAG, "search response is success");
                        String wordMeaning = new JsonResponseNodeTypeDecryption().parse(rb.body().string(), innerWord.getWordName().toLowerCase());
                        outWord = innerWord;
                        outWord.setWordDescription(wordMeaning);
                    } else if (code == 404) {
                        //in case of code 404 outWord returned "null" so user will see Toast "description not found" at doOnSuccess
                        Log.e(Constants.LOG_TAG, "search response is success, but code 404");
                    }
                } catch (IOException e) {
                    Log.e(Constants.LOG_TAG, "search response failure error: " + e.toString());
                }
                return outWord;
            }
        }, callback);

//        myCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    Log.d(Constants.LOG_TAG, "search response is success");
//                    BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Word>() {
//                        @Override
//                        public Word execute() {
//                            Word outWord = null;
//                            try {
//                                String wordMeaning = new JsonResponseNodeTypeDecryption().parse(response.body().string(), innerWord.getWordName().toLowerCase());
//                                outWord = innerWord;
//                                outWord.setWordDescription(wordMeaning);
//
//                            } catch (IOException e) {
//                                Log.e(Constants.LOG_TAG, "success search response body error: " + e.toString());
//                            }
//                            return outWord;
//                        }
//                    }, callback);
//                } else {
//                    Log.e(Constants.LOG_TAG, "search response is success, but code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                Log.e(Constants.LOG_TAG, "search response failure error: " + t.toString());
//            }
//        });
    }
}
