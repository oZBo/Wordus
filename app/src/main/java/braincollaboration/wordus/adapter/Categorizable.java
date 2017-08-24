package braincollaboration.wordus.adapter;

/**
 * An interface for objects that belong to a named category.
 */
public interface Categorizable {

    /**
     * Gets the name of the category that this item belongs to.
     *
     * @return the name <code>String</code>
     */
    String getCategory();

    /**
     * Gets the name of the word that this item belongs to.
     * Need to sort words in alphabetic at recyclerView
     * @return the name <code>String</code>
     */
    String getName();
}
