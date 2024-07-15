package api;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * A utility class for http connection.
 */
public class UrlHandler {

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
    public static HttpsURLConnection createConnection(String urlString, String method, String token, String requestBody) throws IOException {
        @SuppressWarnings("deprecation")
        URL url = new URL(urlString);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
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
}
