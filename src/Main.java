import gui.NetworkCalculator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {




        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("data.json"));
            JSONObject jsonObject = (JSONObject) obj;

            NetworkCalculator networkCalculator = new NetworkCalculator("Netzwerkadressenrechner", (JSONArray) jsonObject.get("data"));

        } catch (IOException | ParseException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", new JSONArray());
            NetworkCalculator networkCalculator = new NetworkCalculator("Netzwerkadressenrechner", (JSONArray) jsonObject.get("data"));
            e.printStackTrace();
        }





    }

}