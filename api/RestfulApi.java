package api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import util.JsonParserUtil;

/**
 * Provides methods to interact with a RESTful API endpoint.
 */
public class RestfulApi {
    private static final String URL_OBJECTS = "https://api.restful-api.dev/objects";

    /**
     * Retrieves all objects from the API.
     *
     * @return JSONArray containing all objects.
     * @throws IOException    If an I/O exception occurs.
     * @throws ParseException If there's an error parsing the JSON response.
     */
    public static JSONArray allObjects() throws IOException, ParseException {
        HttpURLConnection conn = HttpHandler.createConnection(URL_OBJECTS, "GET", null, null);
        return JsonParserUtil.parseStringToArray(HttpHandler.getResponse(conn));
    }

    /**
     * Retrieves a list of objects based on provided IDs.
     *
     * @param ids List of IDs to filter objects.
     * @return JSONArray containing filtered objects.
     * @throws IOException    If an I/O exception occurs.
     * @throws ParseException If there's an error parsing the JSON response.
     */
    public static JSONArray listOfObjects(List<String> ids) throws IOException, ParseException {
        HttpURLConnection conn = HttpHandler.createConnection(HttpHandler.addParametersToUrl(URL_OBJECTS, "id", ids), "GET", null, null);
        return JsonParserUtil.parseStringToArray(HttpHandler.getResponse(conn));
    }

    /**
     * Retrieves a single object by its ID.
     *
     * @param id ID of the object to retrieve.
     * @return JSONObject representing the retrieved object.
     * @throws IOException    If an I/O exception occurs.
     * @throws ParseException If there's an error parsing the JSON response.
     */
    public static JSONObject singleObject(String id) throws IOException, ParseException {
        HttpURLConnection conn = HttpHandler.createConnection(URL_OBJECTS + "/" + id, "GET", null, null);
        return JsonParserUtil.parseStringToObject(HttpHandler.getResponse(conn));
    }

    /**
     * Adds a new object to the API.
     *
     * @param postObject JSONObject representing the object to add.
     * @return JSONObject representing the newly added object.
     * @throws IOException    If an I/O exception occurs.
     * @throws ParseException If there's an error parsing the JSON response.
     */
    public static JSONObject addObject(JSONObject postObject) throws IOException, ParseException {
    HttpURLConnection conn = HttpHandler.createConnection(URL_OBJECTS, "POST", null, postObject.toString());
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = postObject.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return JsonParserUtil.parseStringToObject(HttpHandler.getResponse(conn));
    }

    /**
     * Deletes object to the API.
     *
     * @param String Object Id to delete
     * @return JSONObject result.
     * @throws IOException    If an I/O exception occurs.
     * @throws ParseException If there's an error parsing the JSON response.
     */
    public static JSONObject deleteObject(String id) throws IOException, ParseException {
        HttpURLConnection conn = HttpHandler.createConnection(URL_OBJECTS + "/" + id, "DELETE", null, null);
        return JsonParserUtil.parseStringToObject(HttpHandler.getResponse(conn));
        }
    
}
