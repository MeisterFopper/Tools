package api;

import org.json.simple.JSONObject;

/**
 * Represents a generic device in the Unipi API.
 */
public class UnipiDevice {
    private String dev;
    private String family;
    private String model;
    private Long sn;
    private Long boardCount;
    private String circuit;

    /**
     * Constructs a new UnipiDevice object with the specified parameters.
     *
     * @param dev        The device type.
     * @param family     The family or series of the device.
     * @param model      The model name or number of the device.
     * @param sn         The serial number of the device.
     * @param boardCount The number of boards associated with the device.
     * @param circuit    The circuit identifier for the device.
     */
    public UnipiDevice(String dev, String family, String model, Long sn, Long boardCount, String circuit) {
        this.dev = dev;
        this.family = family;
        this.model = model;
        this.sn = sn;
        this.boardCount = boardCount;
        this.circuit = circuit;
    }

    /**
     * Constructs a new UnipiDevice object from a JSONObject.
     *
     * @param json The JSONObject containing the device information.
     */
    public UnipiDevice(JSONObject json) {
        this.dev = (String) json.get("dev");
        this.family = (String) json.get("family");
        this.model = (String) json.get("model");
        this.sn = (Long) json.get("sn");
        this.boardCount = (Long) json.get("board_count");
        this.circuit = (String) json.get("circuit");
    }

    /**
     * Gets the device type.
     *
     * @return The device type.
     */
    public String getDev() {
        return this.dev;
    }

    /**
     * Gets the device family or series.
     *
     * @return The family or series of the device.
     */
    public String getFamily() {
        return this.family;
    }

    /**
     * Gets the model name or number of the device.
     *
     * @return The model name or number of the device.
     */
    public String getModel() {
        return this.model;
    }

    /**
     * Gets the serial number of the device.
     *
     * @return The serial number of the device.
     */
    public Long getSn() {
        return this.sn;
    }

    /**
     * Gets the number of boards associated with the device.
     *
     * @return The board count.
     */
    public Long getBoardCount() {
        return this.boardCount;
    }

    /**
     * Gets the circuit identifier for the device.
     *
     * @return The circuit identifier.
     */
    public String getCircuit() {
        return this.circuit;
    }

    /**
     * Returns a string representation of this UnipiDevice object.
     *
     * @return A JSON string representing the UnipiDevice object.
     */
    @Override
    public String toString() {
        return toJson().toString();
    }

    /**
     * Converts this UnipiDevice object to a JSONObject.
     *
     * @return A JSONObject representing the UnipiDevice object.
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("dev", this.dev);
        json.put("family", this.family);
        json.put("model", this.model);
        json.put("sn", this.sn);
        json.put("boardCount", this.boardCount);
        json.put("circuit", this.circuit);

        return json;
    }
}
