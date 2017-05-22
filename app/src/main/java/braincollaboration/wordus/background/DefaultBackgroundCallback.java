package braincollaboration.wordus.background;

import android.widget.Toast;

import braincollaboration.wordus.WordusApp;

public abstract class DefaultBackgroundCallback<T> implements IBackgroundCallback<T> {
    @Override
    public void doOnError(Exception e) {
        Toast.makeText(WordusApp.getCurrentActivity(), "Houston, we've got a problem:\n" + e.getMessage(),
                Toast.LENGTH_SHORT).show();
    }
}
