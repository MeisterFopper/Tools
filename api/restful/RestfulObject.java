package api.restful;

import org.json.simple.JSONObject;

public class RestfulObject {
    private static final String STRING_ID = "id";
    private static final String STRING_NAME = "name";
    private static final String STRING_CREATEDAT = "createdAt";
    private static final String STRING_DATE = "data";
    private static final String STRING_MESSAGE = "message";

    private String id;
    private String name;
    private String createdAt;
    private String message;
    private JSONObject data;

    public RestfulObject() {
        reset();
    }

    public void reset() {
        this.id = null;
        this.name = null;
        this.createdAt = null;
        this.message = null;
        this.data = new JSONObject();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return this.data;
    }

    @SuppressWarnings("unchecked")
    public void fromJson(JSONObject json) {
        if (json.containsKey(STRING_ID)) {
            Object value = json.get(STRING_ID);
            setId(((String) value));

            if (json.containsKey(STRING_NAME)) {
                value = json.getOrDefault(STRING_NAME, null);
                setName(((String) value));
            }

            if (json.containsKey(STRING_CREATEDAT)) {
                value = json.getOrDefault(STRING_CREATEDAT, null);
                setCreatedAt(((String) value));
            }

            if (json.containsKey(STRING_DATE)) {
                value = json.getOrDefault(STRING_DATE, new JSONObject());
                setData(((JSONObject) value));
            }
        } else {
            if (json.containsKey(STRING_MESSAGE)) {
                Object value = json.getOrDefault(STRING_MESSAGE, null);
                setMessage(((String) value));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        if (this.id != null) {
            json.put(STRING_ID, this.id);
        }

        if (this.name != null) {
            json.put(STRING_NAME, this.name);
        }

        if (this.createdAt != null) {
            json.put(STRING_CREATEDAT, this.createdAt);
        }

        if (this.data != null) {
            json.put(STRING_DATE, this.data);
        }
        
        if (this.message != null) {
            json.put(STRING_MESSAGE, this.message);
        }

        return json;
    }

}
