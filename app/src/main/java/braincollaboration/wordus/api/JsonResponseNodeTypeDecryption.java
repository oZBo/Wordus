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

public class JsonResponseNodeTypeDecryption {

    public static ArrayList<String> wordMeaningDahl = new ArrayList<>();
    public static ArrayList<String> wordMeaningExplanatory = new ArrayList<>();
    private Iterator<Map.Entry<String, JsonNode>> fieldsIterator;
    private ArrayList <String> listOfMainItems = new ArrayList<>();

    public void parse(String json) {
        fieldsIterator = jsonNode(json);
        String jsonArray = findJsonValue("Items", null);
        listOfMainItems = isItJsonArrayList(jsonArray);
        if (!listOfMainItems.isEmpty()) {
            for (String jsonClass : listOfMainItems) {
                Log.e(Constants.LOG_TAG, jsonClass);
                fieldsIterator = jsonNode(jsonClass);
                if (findJsonValue("Dictionary", null).equals("Dahl (Ru-Ru)")) {
                    String dahlString = findJsonValue("Body", null);
                } else if (findJsonValue("Dictionary", null).equals("Explanatory (Ru-Ru)")) {
                    String explanString = findJsonValue("Body", null);
                }
            }
        }
    }

    private String findJsonValue(String key, String value) {
        String valueString = null;
        while (fieldsIterator != null && fieldsIterator.hasNext()) {

            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            Log.e(Constants.LOG_TAG, "Key: " + field.getKey() + "\tValue: " + field.getValue());

            if ((key != null && value == null) && field.getKey().equals(key)) {
                valueString = field.getValue().toString();
            } else if ((key == null && value != null) && field.getValue().toString().equals(value)) {
                valueString = field.getKey();
            }
        }
        return valueString;
    }

    private ArrayList<String> isItJsonArrayList(String s) {
        ArrayList<String> arrayList = new ArrayList<>();
        int oneArrayInt = 0;
        String oneArrayString = "";

        char[] charArray = s.toCharArray();
        if (charArray[0] == '[' && charArray[charArray.length - 1] == ']') {
            for (int i = 1; i < charArray.length - 1; i++) {
                if (charArray[i] == '{') {
                    oneArrayInt++;
                }
                if (oneArrayInt > 0) {
                    oneArrayString += Character.toString(charArray[i]);
                }
                if (charArray[i] == '}') {
                    oneArrayInt--;
                }
                if (oneArrayInt == 0 && !oneArrayString.equals("")) {
                    arrayList.add(oneArrayString);
                    oneArrayString = "";
                }
            }
        }

        return arrayList;
    }

    private Iterator<Map.Entry<String, JsonNode>> jsonNode(String s) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);

        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(s);
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "JsonRootNode response parse error: " + e.toString());
        }

        return rootNode != null ? rootNode.fields() : null;
    }
}
