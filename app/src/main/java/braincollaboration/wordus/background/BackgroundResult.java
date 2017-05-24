package braincollaboration.wordus.background;

class BackgroundResult<T> {
    private T result;
    private Exception exception;

    public BackgroundResult() {
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}