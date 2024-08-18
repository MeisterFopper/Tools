package api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import util.HttpHandler;
import util.JsonParser;

/**
 * This class provides an API to interact with Unipi devices. It allows you to
 * retrieve device information, set and get relay states, and handle analog outputs.
 * 
 * @author Florian Kaufmann
 * @version 0.1
 */
public class UnipiApi {
    private static final String STRING_VERSION = "version";
    private static final String STRING_DEVICE_INFO = "device_info";
    private static final String STRING_JSON = "json";
    private static final String STRING_VALUE = "value";
    private static final String STRING_RO = "ro";
    private static final String STRING_AO = "ao";

    private String name;
    private String port;

    /**
     * Constructs a UnipiApi instance with the given name and port.
     *
     * @param name The hostname or IP address of the Unipi device.
     * @param port The port number for the API connection.
     */
    public UnipiApi(String name, String port) {
        this.name = name;
        this.port = port;
    }

    /**
     * Gets the name (hostname or IP) of the Unipi device.
     *
     * @return The name of the device.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the port number used for the API connection.
     *
     * @return The port number as a String.
     */
    public String getPort() {
        return this.port;
    }

    /**
     * Builds the base URL using the given name and port.
     *
     * @param name The hostname or IP address of the Unipi device.
     * @param port The port number for the API connection.
     * @return The complete URL as a String.
     * @throws IOException If name or port is null.
     */
    private String getUrl(String name, String port) throws IOException {
        if (name != null && port != null) {
            return "http://" + name + ":" + port;
        } else {
            throw new IOException("Name and Port can not be null.");
        }
    }

    /**
     * Builds a URL with the specified format.
     *
     * @param name   The hostname or IP address of the Unipi device.
     * @param port   The port number for the API connection.
     * @param format The format of the API response (e.g., JSON).
     * @return The formatted URL as a String.
     * @throws IOException If an error occurs while constructing the URL.
     */
    private String getUrlFormat(String name, String port, String format) throws IOException {
        return getUrl(name, port) + "/" + format;
    }

    /**
     * Retrieves the version of the Unipi API.
     *
     * @return The version string of the API.
     * @throws IOException        If an I/O error occurs.
     * @throws URISyntaxException If the URL is not correctly formatted.
     * @throws ParseException     If the response cannot be parsed.
     */
    public String getVersion() throws IOException, URISyntaxException, ParseException {
        HttpURLConnection conn = HttpHandler.createConnection(getUrl(getName(), getPort()) + "/" + STRING_VERSION, "GET", null, null);
        return HttpHandler.getResponse(conn);
    }

    /**
     * Retrieves a list of devices connected to the Unipi device.
     *
     * @return A list of UnipiDevice objects representing each connected device.
     * @throws IOException        If an I/O error occurs.
     * @throws URISyntaxException If the URL is not correctly formatted.
     * @throws ParseException     If the response cannot be parsed.
     */
    public List<UnipiDevice> getDeviceList() throws IOException, URISyntaxException, ParseException {
        List<UnipiDevice> deviceList = new ArrayList<>();
        String sUrl = addCircuit(getUrlFormat(getName(), getPort(), STRING_JSON) + "/" + STRING_DEVICE_INFO, "all");
        HttpURLConnection conn = HttpHandler.createConnection(sUrl, "GET", null, null);
        JSONArray json = JsonParser.toArray(HttpHandler.getResponse(conn));

        for (Object obj : json) {
            JSONObject jsonObject = (JSONObject) obj;
            UnipiDevice deviceInfo = new UnipiDevice(jsonObject);
            deviceList.add(deviceInfo);
        }

        return deviceList;
    }

    /**
     * Retrieves a specific device based on its circuit identifier.
     *
     * @param circuit The circuit identifier of the device.
     * @return A UnipiDevice object representing the specified device.
     * @throws IOException        If an I/O error occurs or the device is not found.
     * @throws URISyntaxException If the URL is not correctly formatted.
     * @throws ParseException     If the response cannot be parsed.
     */
    public UnipiDevice getDevice(String circuit) throws IOException, URISyntaxException, ParseException {
        List<UnipiDevice> deviceList = getDeviceList();
        if (deviceList != null) {
            for (UnipiDevice device : deviceList) {
                if (device.getCircuit().equals(circuit)) {
                    return device;
                }
            }
        }
        throw new IOException("Device not found.");
    }

    /**
     * Sets the state of a relay (RO) on the Unipi device.
     *
     * @param relais The UnipiDeviceRO object containing the relay information to set.
     * @return The updated UnipiDeviceRO object after setting the relay state.
     * @throws IOException        If an I/O error occurs.
     * @throws URISyntaxException If the URL is not correctly formatted.
     * @throws ParseException     If the response cannot be parsed.
     */
    public UnipiDeviceRO setRelais(UnipiDeviceRO relais) throws IOException, URISyntaxException, ParseException {
        String sUrl = getUrlFormat(getName(), getPort(), STRING_JSON) + "/" + STRING_RO + "/" + relais.getCircuit();
        String requestBody = "{\"" + STRING_VALUE + "\": " + relais.getValue() + "}";
        HttpURLConnection conn = HttpHandler.createConnection(sUrl, "POST", null, requestBody);
        return new UnipiDeviceRO(JsonParser.toObject(HttpHandler.getResponse(conn)));
    }

    /**
     * Retrieves the state of a specific relay (RO) based on its circuit identifier.
     *
     * @param circuit The circuit identifier of the relay.
     * @return A UnipiDeviceRO object representing the specified relay.
     * @throws IOException        If an I/O error occurs.
     * @throws URISyntaxException If the URL is not correctly formatted.
     * @throws ParseException     If the response cannot be parsed.
     */
    public UnipiDeviceRO getRelais(String circuit) throws IOException, URISyntaxException, ParseException {
        String sUrl = addCircuit(getUrlFormat(getName(), getPort(), STRING_JSON) + "/" + STRING_RO, circuit);
        HttpURLConnection conn = HttpHandler.createConnection(sUrl, "GET", null, null);
        return new UnipiDeviceRO(JsonParser.toObject(HttpHandler.getResponse(conn)));
    }

    /**
     * Retrieves a list of all relays (RO) connected to the Unipi device.
     *
     * @return A list of UnipiDeviceRO objects representing each connected relay.
     * @throws ParseException     If the response cannot be parsed.
     * @throws IOException        If an I/O error occurs.
     * @throws URISyntaxException If the URL is not correctly formatted.
     */
    public List<UnipiDeviceRO> getRelaisList() throws ParseException, IOException, URISyntaxException {
        List<UnipiDeviceRO> roList = new ArrayList<>();
        String sUrl = addCircuit(getUrlFormat(getName(), getPort(), STRING_JSON) + "/" + STRING_RO, "all");
        HttpURLConnection conn = HttpHandler.createConnection(sUrl, "GET", null, null);
        JSONArray jsonArray = JsonParser.toArray(HttpHandler.getResponse(conn));

        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            UnipiDeviceRO roInfo = new UnipiDeviceRO(jsonObject);
            roList.add(roInfo);
        }

        return roList;
    }

    /**
     * Sets the value of an analog output (AO) on the Unipi device.
     *
     * @param circuit The circuit identifier of the analog output.
     * @param value   The value to set for the analog output.
     * @return The updated UnipiDeviceAO object after setting the value.
     * @throws IOException        If an I/O error occurs.
     * @throws URISyntaxException If the URL is not correctly formatted.
     * @throws ParseException     If the response cannot be parsed.
     */
    public UnipiDeviceAO setAnalogOut(String circuit, String value) throws IOException, URISyntaxException, ParseException {
        String sUrl = getUrlFormat(getName(), getPort(), STRING_JSON) + "/" + STRING_AO + "/" + circuit;
        String requestBody = "{\"" + STRING_VALUE + "\": " + value + "}";
        HttpURLConnection conn = HttpHandler.createConnection(sUrl, "POST", null, requestBody);
        return new UnipiDeviceAO(JsonParser.toObject(HttpHandler.getResponse(conn)));
    }

    /**
     * Retrieves the value of a specific analog output (AO) based on its circuit identifier.
     *
     * @param circuit The circuit identifier of the analog output.
     * @return A UnipiDeviceAO object representing the specified analog output.
     * @throws IOException        If an I/O error occurs.
     * @throws URISyntaxException If the URL is not correctly formatted.
     * @throws ParseException     If the response cannot be parsed.
     */
    public UnipiDeviceAO getAnalogOut(String circuit) throws IOException, URISyntaxException, ParseException {
        String sUrl = getUrlFormat(getName(), getPort(), STRING_JSON) + "/" + STRING_AO + "/" + circuit;
        HttpURLConnection conn = HttpHandler.createConnection(sUrl, "GET", null, null);
        return new UnipiDeviceAO(JsonParser.toObject(HttpHandler.getResponse(conn)));
    }

    /**
     * Retrieves a list of all analog outputs (AO) connected to the Unipi device.
     *
     * @return A list of UnipiDeviceAO objects representing each connected analog output.
     * @throws IOException        If an I/O error occurs.
     * @throws URISyntaxException If the URL is not correctly formatted.
     * @throws ParseException     If the response cannot be parsed.
     */
    public List<UnipiDeviceAO> getAnalogOutList() throws IOException, URISyntaxException, ParseException {
        List<UnipiDeviceAO> aoList = new ArrayList<>();
        String sUrl = getUrlFormat(getName(), getPort(), STRING_JSON) + "/" + STRING_AO + "/all";
        HttpURLConnection conn = HttpHandler.createConnection(sUrl, "GET", null, null);
        JSONArray jsonArray = JsonParser.toArray(HttpHandler.getResponse(conn));

        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            UnipiDeviceAO aoInfo = new UnipiDeviceAO(jsonObject);
            aoList.add(aoInfo);
        }

        return aoList;
    }

    /**
     * Adds a circuit identifier to the URL.
     *
     * @param url     The base URL.
     * @param circuit The circuit identifier to append.
     * @return The complete URL with the circuit identifier.
     * @throws IOException If the circuit identifier is null.
     */
    private String addCircuit(String url, String circuit) throws IOException {
        String sUrl = url;
        if (circuit != null) {
            sUrl = sUrl + "/" + circuit;
        } else {
            throw new IOException("circuit can not be null.");
        }
        return sUrl;
    }

}
