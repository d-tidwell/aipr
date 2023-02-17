package aipr;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSON_result {

    public static void saveMapToJson(Map<String, LinkedList<String>> map, String filename) {
        // Create a new JSON object
        JSONObject json = new JSONObject();

        // Loop through the map and add each entry to the JSON object
        for (Map.Entry<String, LinkedList<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            LinkedList<String> values = entry.getValue();
            JSONArray jsonArray = new JSONArray();
            for (String value : values) {
                jsonArray.add(value);
            }
            json.put(key, jsonArray);
        }

        // Write the JSON object to a file
        try (FileWriter file = new FileWriter(filename)) {
            file.write(json.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
