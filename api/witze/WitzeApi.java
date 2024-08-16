package api.witze;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import util.HttpHandler;
import util.JsonParser;

public class WitzeApi {
    private static final String URL_OBJECTS = "https://witzapi.de/api";

    private static final String STRING_LANGUAGE = "language";
    private static final String STRING_CATEGORY = "category";
    private static final String STRING_JOKE = "joke";
    private static final String STRING_TEXT = "text";
    private static final String STRING_NAME = "name";
    private static final String STRING_ABBREVIATION = "abbreviation";
    private static final String STRING_LIMIT = "limit";

    private String language;
    private String category;

    public WitzeApi() {
        this.language = null;
        this.category = null;
    }

    public void setLanguage(String language) throws URISyntaxException {
        String standardLanguage = null;
        String selectedLanguage = null;

        Map<String, String> languages;
        try {
            languages = loadLanguages();

            for (Map.Entry<String, String> entry : languages.entrySet()) {
                String key = entry.getKey();
                // First entry is standard Language
                if (standardLanguage == null) {
                    standardLanguage = key;
                }
    
                if (key.equals(language)) {
                    selectedLanguage = key;
                }
    
            }

        } catch (IOException | ParseException e) {
            this.language = null;
        }

        if ((standardLanguage != null) && (selectedLanguage == null)) {
            this.language = standardLanguage;
        }

        if (selectedLanguage != null) {
            this.language = selectedLanguage;
        }
        
    }

    public String getLanguage() {
        return this.language;
    }

    public Map<String, String> loadLanguages() throws IOException, ParseException, URISyntaxException {
        String url = URL_OBJECTS + "/" + STRING_LANGUAGE + "/";

        HttpURLConnection conn = HttpHandler.createConnection(url, "GET", null, null);

        JSONArray json = JsonParser.toArray(HttpHandler.getResponse(conn));

        return toHashMap(json, STRING_ABBREVIATION, STRING_LANGUAGE);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    public List<String> getCategories() throws IOException, ParseException, URISyntaxException {
        String url = URL_OBJECTS + "/" + STRING_CATEGORY + "/?" + STRING_LANGUAGE + "=" + this.language;

        HttpURLConnection conn = HttpHandler.createConnection(url, "GET", null, null);

        JSONArray json = JsonParser.toArray(HttpHandler.getResponse(conn));

        return toList(toHashMap(json, STRING_NAME, STRING_LANGUAGE));
    }

    public List<String> getJokes(Integer limit) throws IOException, ParseException, URISyntaxException {
        if (limit == null) {
            limit = 1;
        }

        String url = URL_OBJECTS + "/" + STRING_JOKE + "/?" + STRING_LIMIT + "=" + limit.toString();

        if (this.category != null) {
            url = url + "&" + STRING_CATEGORY + "=" + this.category;
        }

        url = url + "&" + STRING_LANGUAGE + "=" + this.language;

        HttpURLConnection conn = HttpHandler.createConnection(url, "GET", null, null);
        
        JSONArray json = JsonParser.toArray(HttpHandler.getResponse(conn));

        return toList(toHashMap(json, STRING_TEXT, STRING_LANGUAGE));
    }

    public String getJoke() throws IOException, ParseException, URISyntaxException {
        String result = null;
        List<String> jokes = getJokes(1);

        for (String joke : jokes) {
            result = joke;
        }

        return result;
    }

    private Map<String, String> toHashMap(JSONArray json, String keyName, String valueName) {
        Map<String, String> result = new HashMap<>();

        for (Object obj : json) {
            JSONObject jsonObject = (JSONObject) obj;
            String key = (String) jsonObject.get(keyName);
            String value = (String) jsonObject.get(valueName);

            result.put(key, value);
        }

        return result;
    }

    private List<String> toList(Map<String, String> list) throws IOException {
        List<String> result = new ArrayList<>();

        for (Map.Entry<String, String> entry : list.entrySet()) {
            String sName = entry.getKey();
            String sLanguage = entry.getValue(); 

            if (sLanguage.equals(this.language)) {
                result.add(sName);
            } else {
                throw new IOException("Wrong language");
            }
        }

        return result;
    }

}
