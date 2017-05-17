package braincollaboration.wordus.asyncTask;

public interface DeleteWordFromDBCallback {

    // shows toast if db is unavailable
    void dbIsUnavailable();

    // Deleting word from db was finish successfully
    void wordWasDeleted();

}
