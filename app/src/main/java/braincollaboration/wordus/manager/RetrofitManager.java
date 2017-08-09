package braincollaboration.wordus.manager;

import java.util.ArrayList;

import braincollaboration.wordus.api.JsonParse;
import braincollaboration.wordus.background.BackgroundManager;
import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.background.IBackgroundTask;

public class RetrofitManager {

    private static RetrofitManager instance;

    private RetrofitManager() {
    }

    public static synchronized RetrofitManager getInstance() {
        if (instance == null) {
            instance = new RetrofitManager();
        }
        return instance;
    }

    public void searchWordDescription(final String wordName, DefaultBackgroundCallback<String> callback) {
        IBackgroundTask<String> backgroundTask = new IBackgroundTask<String>() {
            @Override
            public String execute() {
                return JsonParse.findWordDescriptionRetrofit(wordName);
            }
        };
        BackgroundManager.getInstance().doUiBlockingBackgroundTask(backgroundTask, callback);


    }
}
