package braincollaboration.wordus.model;


import braincollaboration.wordus.adapter.Categorizable;

public class Word implements Categorizable {

    private String name;
    private String description;
    private int id;
    private boolean hasLookedFor;
    // 1 - new found and never shown word; 0 - not found or shown word;
    private int everShown;

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

    public boolean getHasLookedFor() {
        return hasLookedFor;
    }

    public void setHasLookedFor(boolean hasLookedFor) {
        this.hasLookedFor = hasLookedFor;
    }

    public int getEverShown() {
        return everShown;
    }

    public void setEverShown(int shown) {
        everShown = shown;
    }

    @Override
    public String getCategory() {
        return String.valueOf(name.charAt(0));
    }

    @Override
    public String getName() {
        return name;
    }
}
