package braincollaboration.wordus.api.modelSearch;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Markup__ {

    @SerializedName("FullText")
    @Expose
    private String fullText;
    @SerializedName("Node")
    @Expose
    private String node;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("IsOptional")
    @Expose
    private Boolean isOptional;
    @SerializedName("IsItalics")
    @Expose
    private Boolean isItalics;
    @SerializedName("IsAccent")
    @Expose
    private Boolean isAccent;
    @SerializedName("Markup")
    @Expose
    private List<Markup___> markup = null;

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(Boolean isOptional) {
        this.isOptional = isOptional;
    }

    public Boolean getIsItalics() {
        return isItalics;
    }

    public void setIsItalics(Boolean isItalics) {
        this.isItalics = isItalics;
    }

    public Boolean getIsAccent() {
        return isAccent;
    }

    public void setIsAccent(Boolean isAccent) {
        this.isAccent = isAccent;
    }

    public List<Markup___> getMarkup() {
        return markup;
    }

    public void setMarkup(List<Markup___> markup) {
        this.markup = markup;
    }

}