package util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParserUtil {

    /**
     * Parses a string into a JSONObject.
     * @param jsonString The string to parse.
     * @return The parsed JSONObject.
     * @throws ParseException If the string cannot be parsed into a JSONObject.
     */
    public static JSONObject parseStringToObject(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonString);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        } else {
            throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN, "The provided string is not a valid JSON object.");
        }
    }

    /**
     * Parses a string into a JSONArray.
     * @param jsonString The string to parse.
     * @return The parsed JSONArray.
     * @throws ParseException If the string cannot be parsed into a JSONArray.
     */
    public static JSONArray parseStringToArray(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonString);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        } else {
            throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN, "The provided string is not a valid JSON array.");
        }
    }
}
