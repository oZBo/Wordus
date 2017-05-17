package braincollaboration.wordus.asyncTask;

public interface CheckWordDuplicateCallback {

    void dbNotContainDuplicate();

    void dbContainDuplicate();

    // shows toast if db is unavailable
    void dbIsUnavailable();
}
