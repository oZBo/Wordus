package braincollaboration.wordus.background;

import android.util.Log;
import android.widget.Toast;

import braincollaboration.wordus.WordusApp;
import braincollaboration.wordus.utils.Constants;

public abstract class DefaultBackgroundCallback<T> implements IBackgroundCallback<T> {
    @Override
    public void doOnError(Exception e) {
        Log.e(Constants.LOG_TAG, "Houston, we've got a problem:\n" + e.getMessage());
    }
}
