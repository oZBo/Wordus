package braincollaboration.wordus.asyncTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import braincollaboration.wordus.SQLite.WordusDatabaseHelper;

import static braincollaboration.wordus.asyncTask.DataBaseWorkState.DB_ERROR;
import static braincollaboration.wordus.asyncTask.DataBaseWorkState.WORD_ALREADY_EXIST;
import static braincollaboration.wordus.asyncTask.DataBaseWorkState.WORD_NOT_EXIST;

public class CheckWordDuplicateTask extends AsyncTask<String, Void, DataBaseWorkState> {
    private Context context;
    private CheckWordDuplicateCallback checkWordDuplicateCallback;

    public CheckWordDuplicateTask(Context context, CheckWordDuplicateCallback checkWordDuplicateCallback) {
        this.context = context;
        this.checkWordDuplicateCallback = checkWordDuplicateCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected DataBaseWorkState doInBackground(String... params) {
        String word = params[0];
        SQLiteDatabase db = WordusDatabaseHelper.getReadableDB(context);
        DataBaseWorkState result = DB_ERROR;
        if (db != null) {
            if (!WordusDatabaseHelper.isDBContainAWord(db, word)) {
                result = WORD_NOT_EXIST;
            } else {
                result = WORD_ALREADY_EXIST;
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(DataBaseWorkState result) {
        switch (result) {
            case DB_ERROR:
                checkWordDuplicateCallback.dbIsUnavailable();
                break;
            case WORD_NOT_EXIST:
                checkWordDuplicateCallback.dbNotContainDuplicate();
                break;
            case WORD_ALREADY_EXIST:
                checkWordDuplicateCallback.dbContainDuplicate();
                break;
        }
    }

    /**
     * @return true if currentTask is running
     */

    public boolean isAsyncTaskRunning() {
        return getStatus() == AsyncTask.Status.RUNNING;
    }
}
