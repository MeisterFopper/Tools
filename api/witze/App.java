package api.witze;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

public class App {

    private static void printLanguages(WitzeApi witzeApi) throws IOException, ParseException, URISyntaxException {
        Map<String, String> languages = witzeApi.loadLanguages();

        for (Map.Entry<String, String> entry : languages.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    private static void printCategories(List<String> categories) {
        for (String category : categories) {
            System.out.println(category);
        }
    }

    private static void printJokes(WitzeApi witzeApi, Integer limit) throws IOException, ParseException, URISyntaxException {
        List<String> jokes = witzeApi.getJokes(limit);

        for (String joke : jokes) {
            System.out.println(joke);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Please provide an action: l, c, j");
            return;
        }
        String action = args[0];

        WitzeApi witzeApi = new WitzeApi();        

        switch (action) {
            case "l":
                printLanguages(witzeApi);
                break;

            case "c":
                if (args.length > 1) {
                    String language = args[1];
                    witzeApi.setLanguage(language);
                    System.out.println(witzeApi.getLanguage());
                    printCategories(witzeApi.getCategories());
                } else {
                    System.out.println("Please provide a valid ID or 'all' for the get action");
                }
                break;

            case "j":
                if (args.length > 1) {
                    Integer limit = 1;
                    String language = args[1];

                    if (args.length > 2) {
                        String sLimit = args[2];
                        try {
                            limit = Integer.parseInt(sLimit);
                        } catch (NumberFormatException e) {
                            limit = 1;
                        }
                    } 

                    System.out.println("language: " + language);

                    witzeApi.setLanguage(language);
                    System.out.println("witzeApi.getLanguage(): " + witzeApi.getLanguage());

                    printJokes(witzeApi, limit);
                } else {
                    System.out.println("Please provide at least 2 arguments.");
                }

                break;

            default:
                System.out.println("Unknown action: " + action);
                System.out.println("Please provide an action: l, c, j");
                break;
        }

    }

}
