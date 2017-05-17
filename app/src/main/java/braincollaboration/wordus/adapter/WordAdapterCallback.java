package braincollaboration.wordus.adapter;

import java.util.List;

import braincollaboration.wordus.model.Word;

public interface WordAdapterCallback {

    List<Word> deleteWordItem (Word word);

    void setTopText (String s);

    void setBottomText (String s);

    void changeBottomSheetBehaviorForExpanded();
}
