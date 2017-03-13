package braincollaboration.wordus.model;


import braincollaboration.wordus.adapter.Categorizable;

public class Word implements Categorizable{

    private String wordName;
    private String wordDescription;

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordDescription() {
        return wordDescription;
    }

    public void setWordDescription(String wordDescription) {
        this.wordDescription = wordDescription;
    }

    @Override
    public String getCategory() {
        return String.valueOf(wordName.charAt(0));
    }
}
