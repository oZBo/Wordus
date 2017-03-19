package braincollaboration.wordus.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by braincollaboration on 19/03/2017.
 */

public class AuthResponse {

    @SerializedName("data")
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
