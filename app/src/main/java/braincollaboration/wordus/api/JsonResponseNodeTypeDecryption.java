package braincollaboration.wordus.api;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import braincollaboration.wordus.utils.Constants;

public class JsonResponseNodeTypeDecryption {

    private Map<String, String> mapDictionaries = new HashMap<>();
    private String wordMeaning = "";

    public String parse(String json, String wordName) {
        JsonParse jp = new JsonParse();

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jp.jsonRoot(json);

        String mainItemsArrayValue = jp.findJsonValue("Items", null, fieldsIterator);

        ArrayList<String> listOfMainItems = jp.isItJsonArrayList(mainItemsArrayValue);

        for (String jsonMainItem : listOfMainItems) {
            fieldsIterator = jp.jsonRoot(jsonMainItem);
            String dict = jp.findJsonValue("ArticleId", null, fieldsIterator);

            if (dict.equals("\"Dahl (Ru-Ru)__" + wordName + "\"")) {
                fieldsIterator = jp.jsonRoot(jsonMainItem);
                String dahlDictMeaning = jp.findJsonValue("Body", null, fieldsIterator);
                mapDictionaries.put(Constants.dahlDictionary, dahlDictMeaning);
            } else if (dict.equals("\"Explanatory (Ru-Ru)__" + wordName + "\"")) {
                fieldsIterator = jp.jsonRoot(jsonMainItem);
                String explanDictMeaning = jp.findJsonValue("Body", null, fieldsIterator);
                mapDictionaries.put(Constants.explanatoryDictionary, explanDictMeaning);
            } else if (dict.equals("\"UrbanDictionary (Ru-Ru)__" + wordName + "\"")) {
                fieldsIterator = jp.jsonRoot(jsonMainItem);
                String urbanDictMeaning = jp.findJsonValue("Body", null, fieldsIterator);
                mapDictionaries.put(Constants.urbanDictionary, urbanDictMeaning);
            }
        }

        if (!mapDictionaries.isEmpty()) {
            for (Map.Entry<String, String> dictionary : mapDictionaries.entrySet()) {
                String dictName = dictionary.getKey();

                jp.findDescription(dictionary.getValue());
                dictName += JsonParse.dictionary + "\n\n";
                wordMeaning += dictName;

                JsonParse.dictionary = "";
            }
        }

        return wordMeaning;
    }
}