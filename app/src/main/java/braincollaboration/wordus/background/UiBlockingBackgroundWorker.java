package braincollaboration.wordus.background;


import braincollaboration.wordus.view.loader.LoaderManager;

/**
 * Created by dmitry on 31.03.17.
 */

public class UiBlockingBackgroundWorker<T> extends BackgroundWorker<T> {

    public UiBlockingBackgroundWorker(IBackgroundTask<T> task, IBackgroundCallback<T> callback) {
        super(task, callback);
    }

    @Override
    protected void onPreExecute() {
        LoaderManager.show();
    }

    @Override
    protected void onPostExecute(BackgroundResult<T> result) {
        LoaderManager.hide();
        super.onPostExecute(result);
    }

}
