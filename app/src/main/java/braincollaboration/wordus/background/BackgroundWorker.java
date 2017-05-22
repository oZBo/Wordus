package braincollaboration.wordus.background;

import android.os.AsyncTask;

import braincollaboration.wordus.utils.PerformanceMeter;

class BackgroundWorker<T> extends AsyncTask<Void, Void, BackgroundResult<T>> {
    private IBackgroundTask<T> task;
    private IBackgroundCallback<T> callback;

    public BackgroundWorker(IBackgroundTask<T> task, IBackgroundCallback<T> callback) {
        this.task = task;
        this.callback = callback;
    }

    @Override
    protected BackgroundResult<T> doInBackground(Void... params) {
        BackgroundResult<T> result = new BackgroundResult<>();

        PerformanceMeter.start(task.getClass().getSimpleName());

        try {
            result.setResult(task.execute());
        } catch (Exception e) {
            result.setException(e);
        }

        PerformanceMeter.end(task.getClass().getSimpleName());

        return result;
    }

    @Override
    protected void onPostExecute(BackgroundResult<T> result) {
        if (callback != null) {
            if (result.getException() == null) {
                callback.doOnSuccess(result.getResult());
            } else {
                callback.doOnError(result.getException());
            }
        }
    }
}
