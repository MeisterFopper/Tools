package api;

import org.json.simple.JSONObject;

/**
 * Represents a relay output (RO) device in the Unipi API.
 */
public class UnipiDeviceRO {
    private String dev;
    private String circuit;
    private Long value;

    /**
     * Constructs a new UnipiRo object with the specified parameters.
     *
     * @param dev     The device type (e.g., "ro" for relay output).
     * @param circuit The circuit identifier for the relay output.
     * @param value   The current value of the relay output (0 or 1).
     */
    public UnipiDeviceRO(String dev, String circuit, Long value) {
        this.dev = dev;
        this.circuit = circuit;
        this.value = value;        
    }

    /**
     * Constructs a new UnipiRo object from a JSONObject.
     *
     * @param json The JSONObject containing the device information.
     */
    public UnipiDeviceRO(JSONObject json) {
        this.dev = (String) json.get("dev");
        this.circuit = (String) json.get("circuit");
        this.value = (Long) json.get("value");
    }

    /**
     * Gets the device type.
     *
     * @return The device type (e.g., "ro").
     */
    public String getDev() {
        return this.dev;
    }

    /**
     * Gets the circuit identifier.
     *
     * @return The circuit identifier for the relay output.
     */
    public String getCircuit() {
        return this.circuit;
    }

    /**
     * Gets the current value of the relay output.
     *
     * @return The value of the relay output (0 or 1).
     */
    public Long getValue() {
        return this.value;
    }

    /**
     * Returns a string representation of this UnipiRo object.
     *
     * @return A JSON string representing the UnipiRo object.
     */
    @Override
    public String toString() {
        return toJson().toString();
    }

    /**
     * Converts this UnipiRo object to a JSONObject.
     *
     * @return A JSONObject representing the UnipiRo object.
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("dev", this.dev);
        json.put("circuit", this.circuit);
        json.put("value", this.value);

        return json;
    }
}
