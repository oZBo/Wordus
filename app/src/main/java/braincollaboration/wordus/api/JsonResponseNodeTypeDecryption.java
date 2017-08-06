package braincollaboration.wordus.api;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import braincollaboration.wordus.utils.Constants;
import braincollaboration.wordus.utils.JsonParse;

public class JsonResponseNodeTypeDecryption {

    private String dahlDictMeaning;
    private String explanDictMeaning;
    private ArrayList<String> wordMeaning = new ArrayList<>();

    public ArrayList<String> parse(String json) {
        JsonParse jp = new JsonParse();

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jp.jsonRoot(json);
        String mainItemsArrayValue = jp.findJsonValue("Items", null, fieldsIterator);
        ArrayList<String> listOfMainItems = jp.isItJsonArrayList(mainItemsArrayValue);

        for (String jsonClass : listOfMainItems) {
            fieldsIterator = jp.jsonRoot(jsonClass);
            String dict = jp.findJsonValue("Dictionary", null, fieldsIterator);

            if (dict.equals("\"Dahl (Ru-Ru)\"")) {
                fieldsIterator = jp.jsonRoot(jsonClass);
                dahlDictMeaning = jp.findJsonValue("Body", null, fieldsIterator);
            } else if (dict.equals("\"Explanatory (Ru-Ru)\"")) {
                fieldsIterator = jp.jsonRoot(jsonClass);
                explanDictMeaning = jp.findJsonValue("Body", null, fieldsIterator);
            }
        }

        if (dahlDictMeaning != null) {
            jp.findDescription(dahlDictMeaning);

            dahlDictMeaning = "В. Даль Толковый словарь живого великорусского языка\n";
            dahlDictMeaning += JsonParse.dictionary;
            if (explanDictMeaning != null) {
                dahlDictMeaning += "\n";
            }
            wordMeaning.add(dahlDictMeaning);
        }

        JsonParse.dictionary = "";

        if (explanDictMeaning != null) {
            jp.findDescription(explanDictMeaning);

            explanDictMeaning = "Д.Н. Ушаков Большой современный толковый словарь русского языка\n";
            explanDictMeaning += JsonParse.dictionary;
            wordMeaning.add(explanDictMeaning);
        }

        return wordMeaning;
    }
}