package braincollaboration.wordus.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by braincollaboration on 19/03/2017.
 */

public class WordResponse {

    @SerializedName("Markup")
    private List<MarkupResponse> markups;

    public List<MarkupResponse> getMarkups() {
        return markups;
    }

    public void setMarkups(List<MarkupResponse> markups) {
        this.markups = markups;
    }
}
