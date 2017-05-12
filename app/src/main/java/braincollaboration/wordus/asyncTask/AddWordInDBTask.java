package braincollaboration.wordus.asyncTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import braincollaboration.wordus.SQLite.WordusDatabaseHelper;

import static braincollaboration.wordus.asyncTask.DataBaseWorkState.*;


public class AddWordInDBTask extends AsyncTask<String, Void, DataBaseWorkState> {

    private AddWordInDBCallback addWordInDBCallback;
    private Context context;

    public AddWordInDBTask(Context context, AddWordInDBCallback addWordInDBCallback) {
        this.addWordInDBCallback = addWordInDBCallback;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected DataBaseWorkState doInBackground(String... params) {
        String word = params[0];
        SQLiteDatabase db = WordusDatabaseHelper.getWritableDB(context);
        DataBaseWorkState workState = DB_ERROR;
        if (db != null) {
            if (WordusDatabaseHelper.addInDB(db, word)) {
                workState = WORD_ADDED_SUCCESSFUL;
            } else {
                workState = WORD_ALREADY_EXIST;
            }
        } else {
            return workState;
        }
        return workState;
    }

    @Override
    protected void onPostExecute(DataBaseWorkState result) {
        switch (result){
            case DB_ERROR:
                addWordInDBCallback.dbIsUnavailable();
                break;
            case WORD_ADDED_SUCCESSFUL:
                addWordInDBCallback.wordWasAdded();
                break;
            case WORD_ALREADY_EXIST:
                addWordInDBCallback.dbContainDuplicate();
                break;
        }
    }

    /**
     * @return true if currentTask is running
     */
    public boolean isAsyncTaskRunning(){
        return getStatus() == Status.RUNNING;
    }
}
