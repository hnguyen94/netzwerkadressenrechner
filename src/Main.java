import gui.NetworkCalculator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("data.json"));
            JSONObject jsonObject = (JSONObject) obj;
            NetworkCalculator networkCalculator = new NetworkCalculator("Netzwerkadressenrechner", (JSONArray) jsonObject.get("data"));

        } catch (IOException | ParseException e) {


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", new JSONArray());
            NetworkCalculator networkCalculator = new NetworkCalculator("Netzwerkadressenrechner", (JSONArray) jsonObject.get("data"));
            JOptionPane.showMessageDialog(null, "<html>Schön, dass <b>Sie</b> sich für unseren <b>Netzwerkaddressenrechner</b> entschieden haben!<br>" +
                            "Falls sie <b>Hilfe</b> benötigen, schauen Sie gerne in das <b>Handbuch</b>.<br><br>" +
                            "Es wurde soeben die <i>data.json</i> Datei für die automatische <b>Datenspeicherung</b> angelegt.</html>",
                    "Herzlich Willkommen", JOptionPane.PLAIN_MESSAGE);
        }
    }

}
