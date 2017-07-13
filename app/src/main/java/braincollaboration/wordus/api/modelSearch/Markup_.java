package braincollaboration.wordus.api.modelSearch;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Markup_ {

    @SerializedName("Markup")
    @Expose
    private List<Markup__> markup = null;
    @SerializedName("Node")
    @Expose
    private String node;
    @SerializedName("Text")
    @Expose
    private Object text;
    @SerializedName("IsOptional")
    @Expose
    private Boolean isOptional;
    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("Items")
    @Expose
    private List<Item__> items = null;

    public List<Markup__> getMarkup() {
        return markup;
    }

    public void setMarkup(List<Markup__> markup) {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Item__> getItems() {
        return items;
    }

    public void setItems(List<Item__> items) {
        this.items = items;
    }

}