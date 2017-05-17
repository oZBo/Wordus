package braincollaboration.wordus.asyncTask;

public interface AddWordInDBCallback {

    // shows toast if db is unavailable
    void dbIsUnavailable();

    // Adding new word in db was finish successfully
    void wordWasAdded();

}
