package fr.cpe.microbitprojet.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class RoomInfoList {
    public static List<RoomInfo> jsonToList(String json){
        List<RoomInfo> list = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            Iterator<String> keys = root.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                if (root.get(key) instanceof JSONObject) {
                    RoomInfo roomInfo = new RoomInfo(key, (JSONObject) root.get(key));
                    list.add(roomInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
