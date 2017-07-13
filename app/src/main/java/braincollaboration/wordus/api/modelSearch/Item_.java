package braincollaboration.wordus.api.modelSearch;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item_ {

    @SerializedName("Markup")
    @Expose
    private List<Markup_> markup = null;
    @SerializedName("Node")
    @Expose
    private String node;
    @SerializedName("Text")
    @Expose
    private Object text;
    @SerializedName("IsOptional")
    @Expose
    private Boolean isOptional;

    public List<Markup_> getMarkup() {
        return markup;
    }

    public void setMarkup(List<Markup_> markup) {
        this.markup = markup;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public Boolean getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(Boolean isOptional) {
        this.isOptional = isOptional;
    }

}