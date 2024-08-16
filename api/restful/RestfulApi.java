package api.restful;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import util.HttpHandler;
import util.JsonParser;

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
     * @throws URISyntaxException 
     */
    public static JSONArray allObjects() throws IOException, ParseException, URISyntaxException {
        HttpURLConnection conn = HttpHandler.createConnection(URL_OBJECTS, "GET", null, null);
        return JsonParser.toArray(HttpHandler.getResponse(conn));
    }

    /**
     * Retrieves a list of objects based on provided IDs.
     *
     * @param ids List of IDs to filter objects.
     * @return JSONArray containing filtered objects.
     * @throws IOException    If an I/O exception occurs.
     * @throws ParseException If there's an error parsing the JSON response.
     * @throws URISyntaxException 
     */
    public static JSONArray listOfObjects(List<String> ids) throws IOException, ParseException, URISyntaxException {
        HttpURLConnection conn = HttpHandler.createConnection(addParametersToUrl(URL_OBJECTS, "id", ids), "GET", null, null);
        return JsonParser.toArray(HttpHandler.getResponse(conn));
    }

    public static String addParametersToUrl(String baseUrl, String paramName, List<String> values) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(baseUrl);

        if ((values != null) && (!values.isEmpty())) {
            url.append("?");

            for (int i = 0; i < values.size(); i++) {
                if ((paramName != null) && (!paramName.isEmpty())) {
                    url.append(URLEncoder.encode(paramName, "UTF-8"));
                    url.append("=");
                }
                url.append(URLEncoder.encode(values.get(i), "UTF-8"));
                if (i < values.size() - 1) {
                    url.append("&");
                }
            }
        }

        return url.toString();
    }

    /**
     * Retrieves a single object by its ID.
     *
     * @param id ID of the object to retrieve.
     * @return JSONObject representing the retrieved object.
     * @throws IOException    If an I/O exception occurs.
     * @throws ParseException If there's an error parsing the JSON response.
     * @throws URISyntaxException 
     */
    public static JSONObject singleObject(String id) throws IOException, ParseException, URISyntaxException {
        HttpURLConnection conn = HttpHandler.createConnection(URL_OBJECTS + "/" + id, "GET", null, null);
        return JsonParser.toObject(HttpHandler.getResponse(conn));
    }

    /**
     * Adds a new object to the API.
     *
     * @param postObject JSONObject representing the object to add.
     * @return JSONObject representing the newly added object.
     * @throws IOException    If an I/O exception occurs.
     * @throws ParseException If there's an error parsing the JSON response.
     * @throws URISyntaxException 
     */
    public static JSONObject addObject(JSONObject postObject) throws IOException, ParseException, URISyntaxException {
    HttpURLConnection conn = HttpHandler.createConnection(URL_OBJECTS, "POST", postObject.toString(), null);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = postObject.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return JsonParser.toObject(HttpHandler.getResponse(conn));
    }

    /**
     * Deletes object to the API.
     *
     * @param String Object Id to delete
     * @return JSONObject result.
     * @throws IOException    If an I/O exception occurs.
     * @throws ParseException If there's an error parsing the JSON response.
     * @throws URISyntaxException 
     */
    public static JSONObject deleteObject(String id) throws IOException, ParseException, URISyntaxException {
        HttpURLConnection conn = HttpHandler.createConnection(URL_OBJECTS + "/" + id, "DELETE", null, null);
        return JsonParser.toObject(HttpHandler.getResponse(conn));
        }
    
}
