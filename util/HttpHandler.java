package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.simple.parser.ParseException;

/**
 * A utility class for handling HTTP connections.
 * <p>
 * This class provides methods to create and configure HTTP connections,
 * send JSON request bodies, and handle responses, including error responses.
 * It supports common HTTP methods such as GET, POST, PUT, and DELETE.
 * </p>
 * 
 * <p>
 * The class also includes methods for validating JSON request bodies and 
 * retrieving responses from HTTP connections. 
 * </p>
 * 
 * @author Florian Kaufmann
 * @version 1.0
 */
public class HttpHandler {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    // Common HTTP Methods
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    /**
     * Creates an HTTP connection to the specified URL with the given HTTP method.
     * Optionally sends a JSON request body if provided, and includes an authorization token if specified.
     * 
     * @param urlString   The URL to connect to.
     * @param method      The HTTP method to use for the connection (e.g., GET, POST).
     * @param token       The bearer token to include in the Authorization header. Can be null if no token is needed.
     * @param requestBody The JSON object to send in the request body. Can be null if no body is needed.
     * @return            The initialized HttpURLConnection object.
     * @throws URISyntaxException If the given URL string cannot be parsed as a URI.
     * @throws IOException        If an I/O exception occurs while creating or configuring the connection.
     * @throws ParseException     If the request body is not a valid JSON string.
     */
    public static HttpURLConnection createConnection(String urlString, String method, String token, String requestBody) throws IOException, URISyntaxException, ParseException {
        validateInputs(urlString, method, requestBody);

        HttpURLConnection conn = initializeConnection(urlString, method);
        setRequestProperties(conn, token, requestBody);

        return conn;
    }

    /**
     * Validates input parameters for creating an HTTP connection.
     * 
     * @param urlString   The URL to connect to.
     * @param method      The HTTP method to use for the connection.
     * @param requestBody The JSON object to send in the request body, if any.
     * @throws ParseException     If the request body is not a valid JSON string.
     * @throws NullPointerException If urlString or method is null.
     * @throws IllegalArgumentException If the method is not supported.
     */
    private static void validateInputs(String urlString, String method, String requestBody) throws ParseException {
        if (urlString == null) {
            throw new NullPointerException("The urlString cannot be null.");
        }
        if (method == null) {
            throw new NullPointerException("The method cannot be null.");
        }
        if (!method.equals(GET) && !method.equals(POST) && !method.equals(PUT) && !method.equals(DELETE)) {
            throw new IllegalArgumentException("The HTTP method is not supported. Use GET, POST, PUT, or DELETE.");
        }
        if (requestBody != null) {
            JsonParser.validate(requestBody);  // Assuming JsonParserUtil is the class handling JSON validation
        }
    }

    /**
     * Initializes an HTTP connection to the specified URL with the given HTTP method.
     * 
     * @param urlString The URL to connect to.
     * @param method    The HTTP method to use for the connection.
     * @return          The initialized HttpURLConnection object.
     * @throws IOException If an I/O exception occurs while creating the connection.
     * @throws URISyntaxException If the URL string cannot be parsed as a URI.
     */
    private static HttpURLConnection initializeConnection(String urlString, String method) throws IOException, URISyntaxException {
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_JSON);
        return conn;
    }

    /**
     * Sets the request properties of the HTTP connection, including the authorization token and request body if provided.
     * 
     * @param conn        The HTTP connection to configure.
     * @param token       The bearer token for authorization, or null if not needed.
     * @param requestBody The JSON object to send in the request body, or null if no body is needed.
     * @throws IOException If an I/O exception occurs while sending the request body.
     */
    private static void setRequestProperties(HttpURLConnection conn, String token, String requestBody) throws IOException {
        if (token != null) {
            conn.setRequestProperty(AUTHORIZATION, BEARER + " " + token);
        }

        if (requestBody != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
    }

    /**
     * Retrieves the response from an HTTP connection.
     * 
     * @param conn The HTTP connection to retrieve the response from.
     * @return The response string.
     * @throws IOException If an I/O error occurs or the response code indicates an error.
     */
    public static String getResponse(HttpURLConnection conn) throws IOException {
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                return ""; // No content to return
            } else if (responseCode >= 200 && responseCode < 300) {
                return readResponse(conn);
            } else {
                throw new IOException("HTTP error code: " + responseCode + ", Error response: " + readErrorResponse(conn));
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Reads the response from the input stream of the HTTP connection.
     * 
     * @param conn The HTTP connection from which to read the response.
     * @return The response string.
     * @throws IOException If an I/O error occurs.
     */
    private static String readResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            return readFromBufferedReader(br);
        }
    }

    /**
     * Reads the error response from the error stream of the HTTP connection.
     * 
     * @param conn The HTTP connection from which to read the error response.
     * @return The error response string.
     * @throws IOException If an I/O error occurs.
     */
    private static String readErrorResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
            return readFromBufferedReader(br);
        }
    }

    /**
     * Reads all lines from a BufferedReader and returns them as a single string.
     * 
     * @param br The BufferedReader to read from.
     * @return The concatenated string of all lines.
     * @throws IOException If an I/O error occurs.
     */
    private static String readFromBufferedReader(BufferedReader br) throws IOException {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }
        return response.toString();
    }

}
