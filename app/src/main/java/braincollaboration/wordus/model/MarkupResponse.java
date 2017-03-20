package braincollaboration.wordus.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by braincollaboration on 20/03/2017.
 */

public class MarkupResponse {

    @SerializedName("Markup")
    private List<MarkupResponse> markups;
    @SerializedName("IsItalics")
    private boolean isItalics;
    @SerializedName("IsAccent")
    private boolean isAccent;
    @SerializedName("IsOptional")
    private boolean isOptional;
    @SerializedName("Text")
    private String text;
    @SerializedName("Node")
    private String node;

    public boolean isItalics() {
        return isItalics;
    }

    public void setItalics(boolean italics) {
        isItalics = italics;
    }

    public boolean isAccent() {
        return isAccent;
    }

    public void setAccent(boolean accent) {
        isAccent = accent;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
