package braincollaboration.wordus.api;

import java.util.ArrayList;
import java.util.List;

import braincollaboration.wordus.api.modelSearch.Body;
import braincollaboration.wordus.api.modelSearch.Item;
import braincollaboration.wordus.api.modelSearch.Item_;
import braincollaboration.wordus.api.modelSearch.Item__;
import braincollaboration.wordus.api.modelSearch.Markup;
import braincollaboration.wordus.api.modelSearch.Markup_;
import braincollaboration.wordus.api.modelSearch.Markup__;
import braincollaboration.wordus.api.modelSearch.Markup___;
import braincollaboration.wordus.api.modelSearch.Markup____;
import braincollaboration.wordus.api.modelSearch.Markup_____;
import braincollaboration.wordus.api.modelSearch.MeaningOfTheWord;
import retrofit2.Response;

public class JsonResponseNodeTypeDecryption<T> {
    T in;
    public static ArrayList<String> wordMeaning = new ArrayList<>();

    public JsonResponseNodeTypeDecryption(T in, int itemCount) {
        this.in = in;
        decryptResponse(itemCount);
    }

    public JsonResponseNodeTypeDecryption(T in) {
        this.in = in;
        decryptResponse();
    }

    private void decryptResponse() {
        if (in instanceof Response) {
            MeaningOfTheWord meaningOfTheWord = (MeaningOfTheWord) ((Response) in).body();
            List<Item> items = meaningOfTheWord.getItems();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getDictionary().equals("Dahl (Ru-Ru)")) {
                    wordMeaning.add(i, "Толковый словарь живого великорусского языка В. Даля\n");

                    List<Body> bodies = items.get(i).getBody();
                    for (int a = 0; a < bodies.size(); a++) {
                        new JsonResponseNodeTypeDecryption<>(bodies.get(a), i);
                    }
                } else if (items.get(i).getDictionary().equals("Explanatory (Ru-Ru)")) {
                    wordMeaning.add(i, "Большой современный толковый словарь русского языка\n");

                    List<Body> bodies = items.get(i).getBody();
                    for (int a = 0; a < bodies.size(); a++) {
                        new JsonResponseNodeTypeDecryption<>(bodies.get(a), i);
                    }
                }
            }
        }
    }

    private void decryptResponse(int itemCount) {
        String node;
        if (in instanceof Body) {
            node = ((Body) in).getNode();
            if (node.equals("Paragraph")) {
                List<Markup> markups = ((Body) in).getMarkup();
                for (int i = 0; i < markups.size(); i++) {
                    new JsonResponseNodeTypeDecryption<>(markups.get(i), itemCount);
                }
            } else if (node.equals("List")) {
                List<Item_> items_ = ((Body) in).getItems();
                for (int i = 0; i < items_.size(); i++) {
                    new JsonResponseNodeTypeDecryption<>(items_.get(i), itemCount);
                }
            }
        } else if (in instanceof Markup) {
            node = ((Markup) in).getNode();
            if (node.equals("Text")) {
                wordMeaning.add(itemCount, ((Markup) in).getText());
            }
        } else if (in instanceof Item_) {
            node = ((Item_) in).getNode();
            if (node.equals("ListItem")) {
                List<Markup_> markups_ = ((Item_) in).getMarkup();
                for (int i = 0; i < markups_.size(); i++) {
                    new JsonResponseNodeTypeDecryption<>(markups_.get(i), itemCount);
                }
            }
        } else if (in instanceof Markup_) {
            node = ((Markup_) in).getNode();
            if (node.equals("Paragraph")) {
                List<Markup__> markups__ = ((Markup_) in).getMarkup();
                for (int i = 0; i < markups__.size(); i++) {
                    new JsonResponseNodeTypeDecryption<>(markups__.get(i), itemCount);
                }
            } else if (node.equals("List")) {
                List<Item__> items__ = ((Markup_) in).getItems();
                for (int i = 0; i < items__.size(); i++) {
                    new JsonResponseNodeTypeDecryption<>(items__.get(i), itemCount);
                }
            }
        } else if (in instanceof Markup__) {
            node = ((Markup__) in).getNode();
            if (node.equals( "Text")) {
                wordMeaning.add(itemCount, ((Markup__) in).getText());
            } else if (node.equals("Comment")) {
                List<Markup___> markups___ = ((Markup__) in).getMarkup();
                for (int i = 0; i < markups___.size(); i++) {
                    new JsonResponseNodeTypeDecryption<>(markups___.get(i), itemCount);
                }
            }
        } else if (in instanceof Markup___) {
            node = ((Markup___) in).getNode();
            if (node.equals("Text")) {
                wordMeaning.add(itemCount, ((Markup___) in).getText());
            }
        } else if (in instanceof Item__) {
            node = ((Item__) in).getNode();
            if (node.equals("ListItem")) {
                List<Markup____> markups____ = ((Item__) in).getMarkup();
                for (int i = 0; i < markups____.size(); i++) {
                    new JsonResponseNodeTypeDecryption<>(markups____.get(i), itemCount);
                }
            }
        } else if (in instanceof Markup____) {
            node = ((Markup____) in).getNode();
            if (node.equals("Paragraph")) {
                List<Markup_____> markups = ((Markup____) in).getMarkup();
                for (int i = 0; i < markups.size(); i++) {
                    new JsonResponseNodeTypeDecryption<>(markups.get(i), itemCount);
                }
            }
        } else if (in instanceof Markup_____) {
            node = ((Markup_____) in).getNode();
            if (node.equals("Abbrev")) {
                wordMeaning.add(itemCount, ((Markup_____) in).getText());
            } else if (node.equals("Text")) {
                wordMeaning.add(itemCount, ((Markup_____) in).getText());
            }
        }
    }

}
