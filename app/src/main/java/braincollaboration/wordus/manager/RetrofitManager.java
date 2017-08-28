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

public class RetrofitManager {

    private final static int DICTIONARY_ID = 1049;
    private final static int SEARCH_ZONE = 1;
    private final static int START_INDEX = 0;
    private final static int PAGE_SIZE = 4;

    private RetrofitManager() {
    }

    private static final RetrofitManager ourInstance = new RetrofitManager();

    public static RetrofitManager getInstance() {
        return ourInstance;
    }

    public void searchWordDescription(final Word innerWord, final DefaultBackgroundCallback<Word> callback) {
        ABBYYLingvoAPI abbyyLingvoAPI = ApiController.getInstance();
        final Call<ResponseBody> myCall = abbyyLingvoAPI.getWordMeaning(innerWord.getWordName(), DICTIONARY_ID, DICTIONARY_ID, SEARCH_ZONE, START_INDEX, PAGE_SIZE);

        myCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(Constants.LOG_TAG, innerWord.getWordName() + " description found");
                    BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Word>() {
                        @Override
                        public Word execute() {
                            String wordMeaning = null;

                            try {
                                wordMeaning = response.body().string();
                            } catch (IOException e) {
                                Log.e(Constants.LOG_TAG, "search response is success for : " + innerWord.getWordName() + "; But \"response.body().string();\" try-catch error: " + e.toString());
                            }

                            wordMeaning = new JsonResponseNodeTypeDecryption().parse(wordMeaning, innerWord.getWordName());
                            if (wordMeaning != null) {
                                Log.d(Constants.LOG_TAG, innerWord.getWordName() + " description PARSED");
                                innerWord.setWordDescription(wordMeaning);
                                innerWord.setEverShown(1);
                            } else {
                                Log.d(Constants.LOG_TAG, innerWord.getWordName() + " description NOT PARSED");
                            }
                            innerWord.setHasLookedFor(true);

                            return innerWord;
                        }
                    }, callback);
                } else {
                    Log.e(Constants.LOG_TAG, "Code: " + response.code() + ". Search response isn't successful, there is no description for: " + innerWord.getWordName());
                    BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Word>() {

                        @Override
                        public Word execute() {
                            innerWord.setHasLookedFor(true);
                            return innerWord;
                        }
                    }, callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, "search response failure error: " + t.toString());
                BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Word>() {

                    @Override
                    public Word execute() {
                        return null;
                    }
                }, callback);
            }
        });
    }
}
