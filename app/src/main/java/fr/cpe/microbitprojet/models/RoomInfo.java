package fr.cpe.microbitprojet.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RoomInfo {
    private final String roomName;
    private final Info info;

    public RoomInfo(){
        this.roomName = "roomName";
        this.info = new Info();
    }

    public RoomInfo(String id, String jsonFile){
        this.roomName = id;
        String temperature = "";
        String humidity = "";
        String pressure = "";
        String light = "";
        try {
            JSONObject root = new JSONObject(jsonFile);
            JSONObject room = root.getJSONObject(id);
            temperature = room.getString("T");
            Log.d("Je suis lààà", room.getString("T"));
            humidity = room.getString("H");
            pressure = room.getString("P");
            light = room.getString("L");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.info = new Info(temperature, light, humidity, pressure);
    }

    public RoomInfo(String id, JSONObject jsonObject){
        this.roomName = id;
        String temperature = "";
        String humidity = "";
        String pressure = "";
        String light = "";
        try {
            temperature = jsonObject.getString("T");
            humidity = jsonObject.getString("H");
            pressure = jsonObject.getString("P");
            light = jsonObject.getString("L");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.info = new Info(temperature, light, humidity, pressure);
    }

    public RoomInfo(
            String roomName,
            String temperature,
            String light,
            String humidity,
            String pressure
    ){
        this.info = new Info(temperature, light, humidity, pressure);
        this.roomName = roomName;
    }
    public String getRoomName(){
        return this.roomName;
    }
    public String getTemperature(){
        return this.info.getTemperature();
    }
    public String getHumidity(){
        return this.info.getHumidity();
    }
    public String getPressure(){
        return this.info.getPressure();
    }
    public String getLight(){
        return this.info.getLight();
    }
    public void changeInfo(
            String temperature,
            String humidity,
            String light,
            String pressure
    ){
        this.info.setTemperature(temperature);
        this.info.setHumidity(humidity);
        this.info.setLight(light);
        this.info.setPressure(pressure);
    }

    public void changeInfo(Info info){
        this.changeInfo(
                info.getTemperature(),
                info.getHumidity(),
                info.getLight(),
                info.getPressure());
    }
}
