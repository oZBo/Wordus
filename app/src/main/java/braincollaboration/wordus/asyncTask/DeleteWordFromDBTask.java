package braincollaboration.wordus.asyncTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import braincollaboration.wordus.SQLite.WordusDatabaseHelper;

import static braincollaboration.wordus.asyncTask.DataBaseWorkState.DB_ERROR;
import static braincollaboration.wordus.asyncTask.DataBaseWorkState.WORD_DELETED_SUCCESSFUL;

public class DeleteWordFromDBTask extends AsyncTask<String, Void, DataBaseWorkState> {

    Context context;
    DeleteWordFromDBCallback deleteWordFromDBCallback;

    public DeleteWordFromDBTask(Context context, DeleteWordFromDBCallback deleteWordFromDBCallback) {
        this.context = context;
        this.deleteWordFromDBCallback = deleteWordFromDBCallback;
    }

    @Override
    protected DataBaseWorkState doInBackground(String... params) {
        String word = params[0];
        SQLiteDatabase db = WordusDatabaseHelper.getWritableDB(context);
        DataBaseWorkState workState = DB_ERROR;
        if (db != null) {
            WordusDatabaseHelper.deleteWord(db, word);
            workState = WORD_DELETED_SUCCESSFUL;
        }
        return workState;
    }

    @Override
    protected void onPostExecute(DataBaseWorkState result) {
        switch (result) {
            case DB_ERROR:
                deleteWordFromDBCallback.dbIsUnavailable();
                break;
            case WORD_ADDED_SUCCESSFUL:
                deleteWordFromDBCallback.wordWasDeleted();
                break;
        }
    }

    /**
     * @return true if currentTask is running
     */
    public boolean isAsyncTaskRunning() {
        return getStatus() == Status.RUNNING;
    }
}
