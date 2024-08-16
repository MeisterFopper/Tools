package util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Utility class for parsing and validating JSON strings using the json-simple library.
 * This class provides methods to parse JSON strings into JSONObject and JSONArray instances,
 * as well as a method for validating the structure of JSON strings.
 * 
 * <p>
 * The methods in this class throw a ParseException if the input string is not a valid JSON structure
 * or if it does not match the expected JSON type (object or array).
 * </p>
 * 
 * @author Florian Kaufmann
 * @version 1.0
 */
public class JsonParser {

    private static final JSONParser parser = new JSONParser();

    /**
     * Parses and validates a JSON string.
     *
     * @param jsonString The JSON string to parse and validate.
     * @return The parsed Object.
     * @throws ParseException If the string is not a valid JSON structure.
     */
    private static Object parseAndValidate(String jsonString) throws ParseException {
        return parser.parse(jsonString);
    }

    /**
     * Validates a JSON string.
     *
     * @param jsonString The JSON string to validate.
     * @throws ParseException If the string is not a valid JSON structure.
     */
    public static void validate(String jsonString) throws ParseException {
        parseAndValidate(jsonString);
    }

    /**
     * Parses a string into a JSONObject.
     *
     * @param jsonString The string to parse.
     * @return The parsed JSONObject.
     * @throws ParseException If the string cannot be parsed into a JSONObject or if the string is not a valid JSON object.
     */
    public static JSONObject toObject(String jsonString) throws ParseException {
        Object obj = parseAndValidate(jsonString);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        } else {
            throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN, "The provided string is not a valid JSON object.");
        }
    }

    /**
     * Parses a string into a JSONArray.
     *
     * @param jsonString The string to parse.
     * @return The parsed JSONArray.
     * @throws ParseException If the string cannot be parsed into a JSONArray or if the string is not a valid JSON array.
     */
    public static JSONArray toArray(String jsonString) throws ParseException {
        Object obj = parseAndValidate(jsonString);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        } else {
            throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN, "The provided string is not a valid JSON array.");
        }
    }

}
