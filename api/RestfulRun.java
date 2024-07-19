package api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class RestfulRun {

    @SuppressWarnings("unchecked")
    private static JSONObject testPostJson() {
        JSONObject postJson = new JSONObject();
        JSONObject dataJson = new JSONObject();

        dataJson.put("Generation", "4th");
        dataJson.put("Price", "519.99");
        dataJson.put("Capacity", "256 GB");

        postJson.put("name", "Apple iPad Air");
        postJson.put("data", dataJson);

        return postJson;
    }

    private static void printObject(RestfulObject object) {
        if (object != null) {
            if (object.getMessage() != null) {
                System.out.println(object.getMessage());
            } else {
                System.out.println("id: " + object.getId());
                System.out.println("name: " + object.getName());
            }
        }
    }

    private static void handlePut() {
        try {
            JSONObject json = testPostJson();
            JSONObject resultJson = RestfulApi.addObject(json);
            RestfulObject apiObject = new RestfulObject();
            apiObject.fromJson(resultJson);
            printObject(apiObject);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleList(List<String> ids) {
        try {
            JSONArray resultJson = RestfulApi.listOfObjects(ids);
            System.out.println(resultJson);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleListAll() {
        try {
            JSONArray resultJson = RestfulApi.allObjects();
            System.out.println(resultJson);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleGet(String id) {
        try {
            JSONObject resultJson = RestfulApi.singleObject(id);
            RestfulObject apiObject = new RestfulObject();
            apiObject.fromJson(resultJson);
            printObject(apiObject);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleDelete(String id) {
        try {
            JSONObject resultJson = RestfulApi.deleteObject(id);
            RestfulObject apiObject = new RestfulObject();
            apiObject.fromJson(resultJson);
            printObject(apiObject);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide an action: put, get, delete");
            return;
        }

        String action = args[0];

        switch (action) {
            case "put":
                handlePut();
                break;

            case "get":
                if (args.length == 2) {
                    String idOrCommand = args[1];
                    if (idOrCommand.equals("all")) {
                        handleListAll();
                    } else {
                        handleGet(idOrCommand);
                    }
                } else if (args.length > 2) {
                    List<String> additionalArgs = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
                    handleList(additionalArgs);
                } else {
                    System.out.println("Please provide a valid ID or 'all' for the get action");
                }
                break;

            case "delete":
                if (args.length < 2) {
                    System.out.println("Please provide an ID for the delete action");
                    return;
                }
                handleDelete(args[1]);
                break;

            default:
                System.out.println("Unknown action: " + action);
                System.out.println("Please provide a valid action: add, list, get, delete");
                break;
        }
    }

}
