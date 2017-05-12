package braincollaboration.wordus.asyncTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import braincollaboration.wordus.SQLite.WordusDatabaseHelper;


public class AddWordInDBTask extends AsyncTask<String, Void, Integer> {

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
    protected Integer doInBackground(String... params) {
        String word = params[0];
        SQLiteDatabase db = WordusDatabaseHelper.getWritableDB(context);
        int result = 0;
        if (db != null) {
            if (WordusDatabaseHelper.addInDB(db, word)) {
                result = 1;
            } else {
                result = 2;
            }
        } else {
            return result;
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result == 0) {
            addWordInDBCallback.dbIsUnavailable();
        } else if (result == 1) {
            addWordInDBCallback.wordWasAdded();
        } else {
            addWordInDBCallback.dbContainDuplicate();
        }
    }

    /**
     * @return true if currentTask is running
     */
    public boolean isAsyncTaskRunning(){
        return getStatus() == Status.RUNNING;
    }
}
