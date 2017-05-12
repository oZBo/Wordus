package braincollaboration.wordus.dialog;

import android.text.Editable;

/**
 * Use this callback to transfer received text
 */
public interface SearchDialogCallback {

    /**
     * Editable text is a received text
     */
    void findAWord (Editable text);
}
