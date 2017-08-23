package braincollaboration.wordus.background.broadcast;


import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.ArrayList;
import java.util.List;

import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.manager.DatabaseManager;
import braincollaboration.wordus.manager.RetrofitManager;
import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.utils.Constants;
import braincollaboration.wordus.utils.MyNotification;

public class InternetStatusGCM extends GcmTaskService {
    private static IInternetStatusCallback callback;
    private static int hasntFoundWordsListSize;
    private static int wordsCount;
    private static ArrayList<String> foundWordsList = new ArrayList<>();

    @Override
    public int onRunTask(TaskParams taskParams) {
        switch (taskParams.getTag()) {
            case Constants.TAG_TASK_ONEOFF_LOG:
                Log.d(Constants.LOG_TAG, "task start");
                if (callback == null) {
                    //this method not refresh MainActivity (only suitable for destroyed application)
                    getNotFoundWordList(this);
                } else {
                    // to refresh recyclerView and show Toasts if application is alive
                    callback.internetIsOn();
                }
                return GcmNetworkManager.RESULT_SUCCESS;
            default:
                return GcmNetworkManager.RESULT_FAILURE;
        }
    }

    private void getNotFoundWordList(final Context context) {
        DatabaseManager.getInstance().getNotFoundWordsList(context, new DefaultBackgroundCallback<List<Word>>() {

            @Override
            public void doOnSuccess(List<Word> hasntFoundWordsList) {
                if (!hasntFoundWordsList.isEmpty()) {
                    hasntFoundWordsListSize = hasntFoundWordsList.size();
                    wordsCount = 0;
                    for (Word word : hasntFoundWordsList) {
                        searchWordDescriptionRetrofit(word, context);
                    }
                }
            }
        });
    }

    private void searchWordDescriptionRetrofit(final Word word, final Context context) {
        RetrofitManager.getInstance().searchWordDescription(word, new DefaultBackgroundCallback<Word>() {
            @Override
            public void doOnSuccess(Word result) {
                if (result != null) {
                    addWordDescriptionInDB(result, context);
                }
            }
        });
    }

    private void addWordDescriptionInDB(final Word word, final Context context) {
        DatabaseManager.getInstance().addWordDescriptionInDB(context, word, new DefaultBackgroundCallback<Boolean>() {
            @Override
            public void doOnSuccess(Boolean result) {
                if (result && word.getWordDescription() != null) {
                    foundWordsList.add(word.getWordName());
                }

                wordsCount++;
                if (hasntFoundWordsListSize == wordsCount) {
                    makeNotification();
                }

            }
        });

    }

    private void makeNotification() {
        MyNotification.sendNotification(this, foundWordsList, hasntFoundWordsListSize);
    }

    public static void scheduleSync(Context context, IInternetStatusCallback callback) {
        InternetStatusGCM.callback = callback;
        GcmNetworkManager mGcmNetworkManager = GcmNetworkManager.getInstance(context);
        Task task = new OneoffTask.Builder()
                .setService(InternetStatusGCM.class)
                .setExecutionWindow(0, 30)
                .setTag(Constants.TAG_TASK_ONEOFF_LOG)
                .setUpdateCurrent(false)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setRequiresCharging(false)
                .setPersisted(true)
                .build();

        mGcmNetworkManager.schedule(task);
    }

    public static void cancelGCMTask(Context context) {
        GcmNetworkManager mGcmNetworkManager = GcmNetworkManager.getInstance(context);
        mGcmNetworkManager.cancelTask(Constants.TAG_TASK_ONEOFF_LOG, InternetStatusGCM.class);
    }

    /*called when app is updated to a new version or Google PLay Service are updated to new version.
        This method should be used to reschedule any tasks*/
    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();

        InternetStatusGCM.scheduleSync(this, null);
    }

}
