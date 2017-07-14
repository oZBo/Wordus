package braincollaboration.wordus.adapter;

import java.util.List;

import braincollaboration.wordus.model.Word;

public interface WordAdapterCallback {

    List<Word> deleteWordItem (Word word);

    void setTopTextOfTheBottomSheet (String s);

    void setBottomTextOfTheBottomSheet (String s);

    void changeBottomSheetBehaviorForExpanded();

    List<Word> getDataSet();
}
