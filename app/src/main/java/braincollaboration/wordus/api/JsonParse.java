package braincollaboration.wordus.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import braincollaboration.wordus.utils.Constants;
import braincollaboration.wordus.utils.PerformanceMeter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JsonParse {
    static String dictionary = "";

    void findDescription(String dictionary) {
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator;
        ArrayList<String> arrayList = isItJsonArrayList(dictionary);

        for (String arrayString : arrayList) {
            fieldsIterator = jsonRoot(arrayString);
            String nodeType = findJsonValue("Node", null, fieldsIterator);

            fieldsIterator = jsonRoot(arrayString);
            nodeTypeSwitcher(nodeType, fieldsIterator);
        }
    }

    private void nodeTypeSwitcher(String nodeType, Iterator<Map.Entry<String, JsonNode>> fieldsIterator) {
        String s;
        if (nodeType != null && fieldsIterator != null) {
            switch (nodeType) {
                case "\"Text\"":
                    dictionary += deleteGuillemets(findJsonValue("Text", null, fieldsIterator));
                    break;
                case "\"Abbrev\"":
                    dictionary += deleteGuillemets(findJsonValue("Text", null, fieldsIterator)) + " ";
                    break;
                case "\"Paragraph\"":
                    dictionary += "\n";
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
                    dictionary += "\n";
                    s = findJsonValue("Markup", null, fieldsIterator);
                    new JsonParse().findDescription(s);
                    break;
                case "\"CardRef\"":
                    dictionary += deleteGuillemets(findJsonValue("Text", null, fieldsIterator));
                    break;
                case "\"Ref\"":
                    dictionary += deleteGuillemets(findJsonValue("Text", null, fieldsIterator));
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

    public static ArrayList<String> findWordDescription(String wordName) {
        final ArrayList<String> result = new ArrayList<>();
        ABBYYLingvoAPI abbyyLingvoAPI = Controller.getInstance();

        Call<ResponseBody> myCall = abbyyLingvoAPI.getWordMeaning(wordName, 1049, 1049, 1, 0, 3);

        myCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Log.e(Constants.LOG_TAG, "search response is success");

                    try {
                        ArrayList<String> wordMeaning = new JsonResponseNodeTypeDecryption().parse(response.body().string());
                        result.addAll(wordMeaning);
                    } catch (IOException e) {
                        Log.e(Constants.LOG_TAG, "search RAW response error: " + e.toString());
                    }
                } else {
                    Log.e(Constants.LOG_TAG, "search response isn't successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, "search response failure error: " + t.toString());
            }
        });
        return result;
    }
}
