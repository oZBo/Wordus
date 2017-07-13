package braincollaboration.wordus.api.modelSearch;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeaningOfTheWord {

    @SerializedName("Items")
    @Expose
    private List<Item> items = null;
    @SerializedName("TotalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("HasNextPage")
    @Expose
    private Boolean hasNextPage;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

}