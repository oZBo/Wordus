package braincollaboration.wordus.model;


import braincollaboration.wordus.adapter.Categorizable;

public class Word implements Categorizable{

    private String name;
    private String description;
    private int id;

    public String getWordName() {
        return name;
    }

    public void setWordName(String name) {
        this.name = name;
    }

    public String getWordDescription() {
        return description;
    }

    public void setWordDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getCategory() {
        return String.valueOf(name.charAt(0));
    }
}
