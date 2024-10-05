package app;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LabelGenerator {

    private static List<Integer> genNumbers(Integer start, Integer count) {
        List<Integer> list = new ArrayList<>();
        if (start != null && count != null) {
            for (int i = 0; i < count; i++) {
                list.add(start + i);
            }
        }
        return list;
    }

    private static List<String> genSerialNumbers(String prefix, List<Integer> numbers) {
        List<String> list = new ArrayList<>();
        if (numbers != null) {
            for (Integer number : numbers) {
                list.add(prefix + number.toString());
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static JSONObject toJSON(Integer page, String prefix, List<Integer> numbers) {
        JSONObject json = new JSONObject();

        json.put("page", page);
        json.put("prefix", prefix);

        JSONArray jsonNumbers = new JSONArray();

        for (Integer number : numbers) {
            jsonNumbers.add(number);
        }

        json.put("labels", jsonNumbers);

        return json;
    }

    public static void main(String[] args) {
        String prefix = "AS";
        Integer page = 1;

        List<Integer> numbers = genNumbers(100000, 3);
        List<String> serialNumbers = genSerialNumbers(prefix, numbers);
        
        JSONObject json = toJSON(page, prefix, numbers);

        for (String serialNumber : serialNumbers) {
            System.out.println(serialNumber);
        }

        System.out.println(json);

    }

}
