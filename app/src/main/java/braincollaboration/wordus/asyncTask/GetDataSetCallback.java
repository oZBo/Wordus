package braincollaboration.wordus.asyncTask;

import java.util.List;

import braincollaboration.wordus.model.Word;

public interface GetDataSetCallback {

    void returnDataSet(List<Word> dataSet);
}
