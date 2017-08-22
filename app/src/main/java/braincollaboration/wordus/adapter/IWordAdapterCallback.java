package braincollaboration.wordus.adapter;

import braincollaboration.wordus.model.Word;

/**
 * Default word adapter callback
 */
public interface IWordAdapterCallback {

    /**
     * Notify when item in adapter was clicked
     * @param word entity
     */
    void onItemClicked(Word word);

    /**
     * Notify when delete button clicked
     * @param word entity
     */
    void onItemDeleteButtonClicked(Word word);

}
