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

    public void parse(String json) {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);

        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(json);
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "JsonRootNode response parse error: " + e.toString());
        }

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();

        while (fieldsIterator != null && fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            Log.e(Constants.LOG_TAG, "Key: " + field.getKey() + "\tValue: " + field.getValue());
            if (field.getKey().equals("Items")) {
                isItJsonArrayList(field.getValue().toString());
            }
        }
    }

    private void isItJsonArrayList(String s) {
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
    }
}
