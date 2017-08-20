package braincollaboration.wordus.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.List;

import braincollaboration.wordus.R;
import braincollaboration.wordus.background.BackgroundManager;
import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.background.IBackgroundTask;
import braincollaboration.wordus.database.WordusDatabaseHelper;
import braincollaboration.wordus.manager.RetrofitManager;
import braincollaboration.wordus.model.Word;

public class InternetStatusGCM extends GcmTaskService {
    private static IInternetStatusCallback callback;

    @Override
    public int onRunTask(TaskParams taskParams) {
        switch (taskParams.getTag()) {
            case Constants.TAG_TASK_ONEOFF_LOG:
                Log.d(Constants.LOG_TAG, "task start");
                if (callback == null) {
                    //this method not refresh MainActivity (only suitable for destroyed application)
                    getNotFoundWordList(this.getApplicationContext());
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
        BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<List<Word>>() {

            @Override
            public List<Word> execute() {
                return WordusDatabaseHelper.getNotFoundWordDataSet(context);
            }
        }, new DefaultBackgroundCallback<List<Word>>() {

            @Override
            public void doOnSuccess(List<Word> result) {
                if (!result.isEmpty()) {
                    for (Word word : result) {
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
                    if (result.getWordDescription() == null) {
                        Log.d(Constants.LOG_TAG, word.getWordName() + " " + getString(R.string.description_not_found));
                    }
                }
            }
        });
    }

    private void addWordDescriptionInDB(final Word word, final Context context) {
        BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Boolean>() {

            @Override
            public Boolean execute() {
                SQLiteDatabase db = WordusDatabaseHelper.getReadableDB(context);
                if (db != null && WordusDatabaseHelper.isDBContainAWord(db, word.getWordName())) {
                    db = WordusDatabaseHelper.getWritableDB(context);
                    if (db != null) {
                        WordusDatabaseHelper.addWordDescriptionInDB(db, word.getWordName(), word.getWordDescription(), word.isHasLookedFor());
                        return true;
                    }
                }
                return false;
            }
        }, new DefaultBackgroundCallback<Boolean>() {
            @Override
            public void doOnSuccess(Boolean result) {
                if (result) {
                    ifLastWordMakeNotification();
                    if (word.getWordDescription() != null) {
                        Log.d(Constants.LOG_TAG, word.getWordName() + " " + getString(R.string.description_found));
                    }
                }
            }
        });
    }

    private void ifLastWordMakeNotification() {
        // notification procedure
    }

    public static void scheduleSync(Context context, IInternetStatusCallback callback) {
        if (callback != null) {
            InternetStatusGCM.callback = callback;
        }
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
