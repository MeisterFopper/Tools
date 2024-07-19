package api;

import org.json.simple.JSONObject;

public class RestfulObject {
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
        if (json.containsKey("id")) {
            Object value = json.get("id");
            setId(((String) value));

            if (json.containsKey("name")) {
                value = json.getOrDefault("name", "");
                setName(((String) value));
            }

            if (json.containsKey("createdAt")) {
                value = json.getOrDefault("createdAt", "");
                setCreatedAt(((String) value));
            }

            if (json.containsKey("data")) {
                value = json.getOrDefault("data", "");
                setData(((JSONObject) value));
            }
        } else {
            if (json.containsKey("message")) {
                Object value = json.getOrDefault("message", "");
                setMessage(((String) value));
            }
        }
    }
}
