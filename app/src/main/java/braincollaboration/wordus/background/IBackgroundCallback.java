package braincollaboration.wordus.background;

public interface IBackgroundCallback<T> {
    void doOnSuccess(T result);
    void doOnError(Exception e);
}
