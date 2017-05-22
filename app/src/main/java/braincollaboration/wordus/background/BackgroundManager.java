package braincollaboration.wordus.background;

public class BackgroundManager {
    private static BackgroundManager instance = new BackgroundManager();

    public static BackgroundManager getInstance() {
        return instance;
    }

    private BackgroundManager() {
    }

    public <T> void doBackgroundTask(IBackgroundTask<T> task) {
        doBackgroundTask(task, null);
    }

    public <T> void doBackgroundTask(IBackgroundTask<T> task, IBackgroundCallback<T> callback) {
        new BackgroundWorker<>(task, callback).execute();
    }

    public <T> void doUiBlockingBackgroundTask(IBackgroundTask<T> task) {
        doUiBlockingBackgroundTask(task, null);
    }

    public <T> void doUiBlockingBackgroundTask(IBackgroundTask<T> task, IBackgroundCallback<T> callback) {
        new UiBlockingBackgroundWorker<>(task, callback).execute();
    }
}
