package api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import util.JsonParserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Taktiq Vehicle with order details, model, description, dates, and features.
 */
public class TaktiqVehicles {
    static final String ORDER_NUMBER_STRING = "orderNumber";
    static final String MODEL_STRING = "model";
    static final String DESCRIPTION_STRING = "description";
    static final String FEATURES_STRING = "features";
    static final String DATES_STRING = "dates";
    static final String DATE_STRING = "date";
    static final String LOCATION_STRING = "location";
    static final String TYPE_STRING = "type";

    private String orderNumber;
    private String model;
    private String description;
    private String seriesNumber;

    private List<DateEntry> datesList;
    private List<String> featuresList;

    /**
     * Default constructor to initialize lists for dates and features.
     */
    public TaktiqVehicles() {
        datesList = new ArrayList<>();
        featuresList = new ArrayList<>();
    }

    /**
     * Sets the order number of the vehicle.
     *
     * @param orderNumber The order number of the vehicle.
     */
    public void setOrderNumber(String orderNumber) {
        if (orderNumber != null && orderNumber.length() == 9) {
            this.orderNumber = orderNumber;
            this.seriesNumber = orderNumber.substring(0, 4);
        }
    }

    /**
     * Gets the order number of the vehicle.
     *
     * @param orderNumber The order number of the vehicle.
     */
    public String getOrderNumber() {
        return this.orderNumber;
    }

    /**
     * Gets the series number of the vehicle.
     *
     * @param orderNumber The number number of the vehicle.
     */
    public String getSeriesNumber() {
        return this.seriesNumber;
    }

    /**
     * Sets the model of the vehicle.
     *
     * @param model The model name.
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Sets the description of the vehicle.
     *
     * @param description The description of the vehicle.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the list of features for the vehicle.
     *
     * @param featuresList The list of features.
     */
    public void setFeaturesList(List<String> featuresList) {
        featuresList.add(this.seriesNumber);
        this.featuresList = featuresList;
    }

    /**
     * Adds a date entry to the vehicle.
     *
     * @param date     The date of the event.
     * @param location The location of the event.
     * @param type     The type of the event.
     */
    public void addDate(String date, String location, String type) {
        datesList.add(new DateEntry(date, location, type));
    }

    /**
     * Parses a JSON string representing a TaktiqVehicle object and sets its fields accordingly.
     *
     * @param jsonString The JSON string representing the TaktiqVehicle object.
     * @throws ParseException If parsing of JSON fails.
     */
    @SuppressWarnings("unchecked")
    public void loadVehicleFromJson(String jsonString) throws ParseException {
        JSONObject jsonObject = JsonParserUtil.parseStringToObject(jsonString);

        // Set orderNumber
        this.orderNumber = (String) jsonObject.getOrDefault(ORDER_NUMBER_STRING, null);

        // Set model
        this.model = (String) jsonObject.getOrDefault(MODEL_STRING, null);

        // Set description
        this.description = (String) jsonObject.getOrDefault(DESCRIPTION_STRING, null);

        // Set datesList
        if (jsonObject.containsKey(DATES_STRING)) {
            JSONArray datesArray = (JSONArray) jsonObject.get(DATES_STRING);
            for (Object dateObj : datesArray) {
                JSONObject dateObject = (JSONObject) dateObj;
                String date = (String) dateObject.getOrDefault(DATE_STRING, null);
                String location = (String) dateObject.getOrDefault(LOCATION_STRING, null);
                String type = (String) dateObject.getOrDefault(TYPE_STRING, null);

                datesList.add(new DateEntry(date, location, type));
            }
        }

        // Set featuresList
        if (jsonObject.containsKey(FEATURES_STRING)) {
            JSONArray featuresArray = (JSONArray) jsonObject.get(FEATURES_STRING);
            for (Object featureObj : featuresArray) {
                if (featureObj instanceof String) {
                    String feature = (String) featureObj;
                    featuresList.add(feature);
                }
            }
        }
    }

    /**
     * Converts the TaktiqVehicle object to a JSON-like string representation.
     *
     * @return JSON-like string representing the TaktiqVehicle object.
     */
    @SuppressWarnings("unchecked")
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ORDER_NUMBER_STRING, orderNumber);
        jsonObject.put(MODEL_STRING, model);
        jsonObject.put(DESCRIPTION_STRING, description);
        jsonObject.put(DATES_STRING, datesListToJson());
        jsonObject.put(FEATURES_STRING, featuresList);

        return jsonObject.toJSONString();
    }

    /**
     * Converts the dates list to a JSON array.
     *
     * @return JSON array representing the dates list.
     */
    @SuppressWarnings("unchecked")
    private JSONArray datesListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (DateEntry dateEntry : datesList) {
            JSONObject dateObject = new JSONObject();
            dateObject.put(DATE_STRING, dateEntry.getDate());
            dateObject.put(LOCATION_STRING, dateEntry.getLocation());
            dateObject.put(TYPE_STRING, dateEntry.getType());
            jsonArray.add(dateObject);
        }
        return jsonArray;
    }

    /**
     * Represents a date entry with date, location, and type.
     */
    private static class DateEntry {
        private String date;
        private String location;
        private String type;

        /**
         * Constructs a DateEntry object with specified date, location, and type.
         *
         * @param date     The date of the event (must not be null).
         * @param location The location of the event (must not be null).
         * @param type     The type of the event (must not be null).
         */
        public DateEntry(String date, String location, String type) {
            this.date = date;
            this.location = location;
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public String getLocation() {
            return location;
        }

        public String getType() {
            return type;
        }
    }
}
