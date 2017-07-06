package braincollaboration.wordus.api.modelArticle;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeaningOfTheWord {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("TitleMarkup")
    @Expose
    private List<TitleMarkup> titleMarkup = null;
    @SerializedName("Dictionary")
    @Expose
    private String dictionary;
    @SerializedName("ArticleId")
    @Expose
    private String articleId;
    @SerializedName("Body")
    @Expose
    private List<Body> body = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TitleMarkup> getTitleMarkup() {
        return titleMarkup;
    }

    public void setTitleMarkup(List<TitleMarkup> titleMarkup) {
        this.titleMarkup = titleMarkup;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public List<Body> getBody() {
        return body;
    }

    public void setBody(List<Body> body) {
        this.body = body;
    }

}