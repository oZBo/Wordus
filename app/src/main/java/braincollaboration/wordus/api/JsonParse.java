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

class JsonParse {
    static String dictionary = "";

    void findDescription(String dictionary) {
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator;
        ArrayList<String> arrayList = isItJsonArrayList(dictionary);

        for (String arrayString : arrayList) {
            fieldsIterator = jsonRoot(arrayString);
            String nodeType = findJsonValue("Node", null, fieldsIterator);

            nodeTypeSwitcher(nodeType, arrayString);
        }
    }

    private void nodeTypeSwitcher(String nodeType, String arrayString) {
        String s;
        String compare;
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonRoot(arrayString);
        Iterator<Map.Entry<String, JsonNode>> fieldsIteratorSpare;
        if (nodeType != null && fieldsIterator != null) {
            switch (nodeType) {
                case "\"Text\"":
                    s = deleteGuillemets(findJsonValue("Text", null, fieldsIterator));

                    fieldsIteratorSpare = jsonRoot(arrayString);
                    compare = findJsonValue("IsItalics", null, fieldsIteratorSpare);
                    if (compare != null && compare.equals("true")) {
                        dictionary += "<i>" + s + "</i>";
                    } else {
                        dictionary += s;
                    }
                    break;
                case "\"Abbrev\"":
                    s = deleteGuillemets(findJsonValue("Text", null, fieldsIterator));
                    dictionary += s;
                    break;
                case "\"Paragraph\"":
                    dictionary += "<br>    ";
                    s = findJsonValue("Markup", null, fieldsIterator);
                    new JsonParse().findDescription(s);
                    break;
                case "\"List\"":
                    s = findJsonValue("Items", null, fieldsIterator);
                    new JsonParse().findDescription(s);
                    break;
                case "\"ListItem\"":
                    s = findJsonValue("Markup", null, fieldsIterator);
                    new JsonParse().findDescription(s);
                    break;
                case "\"Comment\"":
                    s = findJsonValue("Markup", null, fieldsIterator);
                    new JsonParse().findDescription(s);
                    break;
                case "\"Examples\"":
                    s = findJsonValue("Items", null, fieldsIterator);
                    new JsonParse().findDescription(s);
                    break;
                case "\"ExampleItem\"":
                    s = findJsonValue("Markup", null, fieldsIterator);
                    new JsonParse().findDescription(s);
                    break;
                case "\"Example\"":
                    dictionary += "<br>";
                    s = findJsonValue("Markup", null, fieldsIterator);
                    new JsonParse().findDescription(s);
                    break;
                case "\"CardRef\"":
                    s = deleteGuillemets(findJsonValue("Text", null, fieldsIterator));
                    dictionary += s;
                    break;
                case "\"Ref\"":
                    s = deleteGuillemets(findJsonValue("Text", null, fieldsIterator));
                    dictionary += s;
                    break;
            }
        }

    }

    String findJsonValue(String key, String value, Iterator<Map.Entry<String, JsonNode>> fieldsIterator) {
        String s = null;
        while (fieldsIterator != null && fieldsIterator.hasNext()) {

            Map.Entry<String, JsonNode> field = fieldsIterator.next();

            if (value == null && field.getKey().equals(key)) {
                s = field.getValue().toString();
                fieldsIterator = null;
            } else if (key == null && field.getValue().toString().equals(value)) {
                s = field.getKey();
                fieldsIterator = null;
            }
        }
        return s;
    }

    private String deleteGuillemets(String s) {
        char[] charS = s.toCharArray();

        if (charS[0] == '"' && charS[charS.length - 1] == '"') {

            s = "";
            for (int i = 1; i < charS.length - 1; i++) {
                s += Character.toString(charS[i]);
            }
        }
        return s;
    }

    ArrayList<String> isItJsonArrayList(String s) {
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
        } else {
            arrayList.add(s);
        }

        return arrayList;
    }

    Iterator<Map.Entry<String, JsonNode>> jsonRoot(String s) {
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
