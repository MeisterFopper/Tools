package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A utility class to handle HTTP connection.
 */
public class HttpHandler {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    /**
     * Adds a query parameter to the given URL.
     * 
     * @param baseUrl   The base URL to which the parameter will be added.
     * @param paramName The name of the query parameter, is optional.
     * @param value     The value of the query parameter.
     * @return The modified URL with the added query parameter.
     * @throws UnsupportedEncodingException If encoding the parameter value fails.
     */
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
     * Creates an HTTPS connection to the specified URL with the given HTTP method.
     * Optionally sends a JSON request body if provided.
     * 
     * @param urlString   The URL to connect to.
     * @param method      The HTTP method (e.g., GET, POST).
     * @param requestBody The JSON object to send in the request body. Can be null if no body is needed.
     * @return            The initialized HttpsURLConnection object.
     * @throws IOException If an I/O exception occurs while creating or configuring the connection.
     */
    public static HttpURLConnection createConnection(String urlString, String method, String token, String requestBody) throws IOException {
        @SuppressWarnings("deprecation")
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_JSON);

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

        return conn;
    }

    /**
     * Retrieves the response from an HTTP connection.
     * 
     * @param conn The HTTP connection.
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
     * @param conn The HTTP connection.
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
     * @param conn The HTTP connection.
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
