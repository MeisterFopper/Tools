package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

/**
 * A utility class to handle HTTP responses.
 */
public class HttpResponseHandler {

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
