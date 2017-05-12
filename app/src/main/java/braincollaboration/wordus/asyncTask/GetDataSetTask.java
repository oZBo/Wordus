package braincollaboration.wordus.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import braincollaboration.wordus.SQLite.WordusDatabaseHelper;
import braincollaboration.wordus.model.Word;

public class GetDataSetTask extends AsyncTask<Void, Void, List<Word>> {

    private Context context;
    private GetDataSetCallback getDataSetCallback;

    public GetDataSetTask(Context context, GetDataSetCallback getDataSetCallback) {
        this.context =  context;
        this.getDataSetCallback = getDataSetCallback;
    }

    @Override
    protected List<Word> doInBackground(Void... params) {
        return WordusDatabaseHelper.getDataSet(context);
    }

    @Override
    protected void onPostExecute(List<Word> words) {
        if (words != null) {
            getDataSetCallback.returnDataSet(words);
        }
    }

    /**
     * @return true if currentTask is running
     */
    public boolean isAsyncTaskRunning(){
        return getStatus() == Status.RUNNING;
    }
}
