package api;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents a device with various modes, values, and circuit information.
 */
public class UnipiDeviceAO {
    private String dev;
    private String mode;
    private String unit;
    private Map<String, Mode> modes;
    private Double value;
    private String circuit;

    /**
     * Constructs a new UnipiDeviceAO object with the specified parameters.
     *
     * @param dev     The device type (e.g., "ao").
     * @param mode    The current mode of the device (e.g., "Voltage").
     * @param unit    The unit of measurement (e.g., "V").
     * @param modes   A map of modes available for the device.
     * @param value   The current value of the device.
     * @param circuit The circuit identifier for the device.
     */
    public UnipiDeviceAO(String dev, String mode, String unit, Map<String, Mode> modes, Double value, String circuit) {
        this.dev = dev;
        this.mode = mode;
        this.unit = unit;
        this.modes = modes;
        this.value = value;
        this.circuit = circuit;
    }

    /**
     * Constructs a new UnipiDeviceAO object from a JSONObject.
     *
     * @param json The JSONObject containing the device information.
     */
    public UnipiDeviceAO(JSONObject json) {
        this.dev = (String) json.get("dev");
        this.mode = (String) json.get("mode");
        this.unit = (String) json.get("unit");
        this.value = (Double) json.get("value");
        this.circuit = (String) json.get("circuit");

        this.modes = new HashMap<>();
        JSONObject modesJson = (JSONObject) json.get("modes");
        for (Object key : modesJson.keySet()) {
            String modeKey = (String) key;
            JSONObject modeDetails = (JSONObject) modesJson.get(modeKey);
            Mode mode = new Mode(modeDetails);
            this.modes.put(modeKey, mode);
        }
    }

    /**
     * Gets the device type.
     *
     * @return The device type as a String.
     */
    public String getDev() {
        return dev;
    }

    /**
     * Gets the current mode of the device.
     *
     * @return The current mode as a String.
     */
    public String getMode() {
        return mode;
    }

    /**
     * Gets the unit of measurement for the device.
     *
     * @return The unit of measurement as a String.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Gets the map of modes available for the device.
     *
     * @return A map where the keys are mode names and the values are Mode objects.
     */
    public Map<String, Mode> getModes() {
        return modes;
    }

    /**
     * Gets the current value of the device.
     *
     * @return The current value as a Double.
     */
    public Double getValue() {
        return value;
    }

    /**
     * Gets the circuit identifier for the device.
     *
     * @return The circuit identifier as a String.
     */
    public String getCircuit() {
        return circuit;
    }

    /**
     * Converts this UnipiDeviceAO object to a string representation of its JSON format.
     *
     * @return A JSON string representing the UnipiDeviceAO object.
     */
    @Override
    public String toString() {
        return toJson().toString();
    }

    /**
     * Converts this UnipiDeviceAO object to a JSONObject.
     *
     * @return A JSONObject representing the UnipiDeviceAO object.
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("dev", this.dev);
        json.put("mode", this.mode);
        json.put("unit", this.unit);
        json.put("value", this.value);
        json.put("circuit", this.circuit);

        JSONObject modesJson = new JSONObject();
        for (Map.Entry<String, Mode> entry : this.modes.entrySet()) {
            modesJson.put(entry.getKey(), entry.getValue().toJson());
        }
        json.put("modes", modesJson);

        return json;
    }

    /**
     * Represents a mode with specific attributes.
     */
    public static class Mode {
        private String unit;
        private Double value;
        private Double[] range;

        /**
         * Constructs a new Mode object with the specified parameters.
         *
         * @param unit  The unit of measurement for the mode.
         * @param value The value associated with the mode.
         * @param range The range of values the mode can take (optional).
         */
        public Mode(String unit, Double value, Double[] range) {
            this.unit = unit;
            this.value = value;
            this.range = range;
        }

        /**
         * Constructs a new Mode object from a JSONObject.
         *
         * @param json The JSONObject containing the mode information.
         */
        public Mode(JSONObject json) {
            this.unit = (String) json.get("unit");
            this.value = json.get("value") instanceof Number ? ((Number) json.get("value")).doubleValue() : null;

            JSONArray rangeArray = (JSONArray) json.get("range");
            if (rangeArray != null) {
                this.range = new Double[rangeArray.size()];
                for (int i = 0; i < rangeArray.size(); i++) {
                    this.range[i] = ((Number) rangeArray.get(i)).doubleValue();
                }
            }
        }

        /**
         * Gets the unit of measurement for the mode.
         *
         * @return The unit of measurement as a String.
         */
        public String getUnit() {
            return unit;
        }

        /**
         * Gets the value associated with the mode.
         *
         * @return The value as a Double.
         */
        public Double getValue() {
            return value;
        }

        /**
         * Gets the range of values the mode can take.
         *
         * @return An array of Double representing the range, or null if not applicable.
         */
        public Double[] getRange() {
            return range;
        }

        /**
         * Converts this Mode object to a string representation of its JSON format.
         *
         * @return A JSON string representing the Mode object.
         */
        @Override
        public String toString() {
            return toJson().toString();
        }

        /**
         * Converts this Mode object to a JSONObject.
         *
         * @return A JSONObject representing the Mode object.
         */
        @SuppressWarnings("unchecked")
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("unit", this.unit);
            json.put("value", this.value);

            if (this.range != null) {
                JSONArray rangeArray = new JSONArray();
                for (Double r : this.range) {
                    rangeArray.add(r);
                }
                json.put("range", rangeArray);
            }

            return json;
        }
    }
}
