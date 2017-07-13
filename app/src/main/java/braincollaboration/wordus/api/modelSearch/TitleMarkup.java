package braincollaboration.wordus.api.modelSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TitleMarkup {

    @SerializedName("IsItalics")
    @Expose
    private Boolean isItalics;
    @SerializedName("IsAccent")
    @Expose
    private Boolean isAccent;
    @SerializedName("Node")
    @Expose
    private String node;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("IsOptional")
    @Expose
    private Boolean isOptional;

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

}