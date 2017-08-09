package braincollaboration.wordus.api;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import braincollaboration.wordus.utils.Constants;
import braincollaboration.wordus.utils.PerformanceMeter;

public class JsonResponseNodeTypeDecryption {

    private String dahlDictMeaning;
    private String explanDictMeaning;
    private String urbanDictMeaning;
    private Map<String, String> mapDictionaries = new HashMap<>();
    private String wordMeaning = "";

    public String parse(String json) {
        JsonParse jp = new JsonParse();

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jp.jsonRoot(json);

        String mainItemsArrayValue = jp.findJsonValue("Items", null, fieldsIterator);

        ArrayList<String> listOfMainItems = jp.isItJsonArrayList(mainItemsArrayValue);

        for (String jsonMainItem : listOfMainItems) {
            fieldsIterator = jp.jsonRoot(jsonMainItem);
            String dict = jp.findJsonValue("Dictionary", null, fieldsIterator);

            switch (dict) {
                case "\"Dahl (Ru-Ru)\"":
                    fieldsIterator = jp.jsonRoot(jsonMainItem);
                    dahlDictMeaning = jp.findJsonValue("Body", null, fieldsIterator);
                    mapDictionaries.put(Constants.dahlDictionary, dahlDictMeaning);
                    break;
                case "\"Explanatory (Ru-Ru)\"":
                    fieldsIterator = jp.jsonRoot(jsonMainItem);
                    explanDictMeaning = jp.findJsonValue("Body", null, fieldsIterator);
                    mapDictionaries.put(Constants.explanatoryDictionary, explanDictMeaning);
                    break;
                case "\"UrbanDictionary (Ru-Ru)\"":
                    fieldsIterator = jp.jsonRoot(jsonMainItem);
                    urbanDictMeaning = jp.findJsonValue("Body", null, fieldsIterator);
                    mapDictionaries.put(Constants.urbanDictionary, urbanDictMeaning);
                    break;
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