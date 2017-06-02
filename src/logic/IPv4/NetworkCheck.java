package logic.IPv4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.swing.*;
import java.util.Arrays;


/**
 * Created by Maik on 01.06.2017
 */
public class NetworkCheck {
    private JSONArray data;
    private String[] ipDB;
    private JSONObject networkObject = null;

    public NetworkCheck(JSONArray data){
        this.data = data;
        ipDB = new String[data.size()];
    }

    public boolean isNetworkPossible(String networkAdress){
        for (int i = 0; i < data.size(); i++) {
            networkObject = (JSONObject) data.get(i);
            ipDB[i] = networkObject.get("id").toString();

        }
        return false;
    }
}
