package braincollaboration.wordus.asyncTask;

public interface AddWordInDBCallback {

    // shows toast if db is unavailable
    void dbIsUnavailable();

    // shows toast that db is already contain same word
    void dbContainDuplicate();

    // Adding new word in db was finish successfully
    void wordWasAdded();

}
