package fr.cpe.microbitprojet.models;

public class Info {
    private String temperature;
    private String humidity;
    private String light;
    private String pressure;

    public Info(){
        this.humidity = "humidity";
        this.temperature = "temperature";
        this.light = "light";
        this.pressure = "pressure";
    }

    public Info(
            String temperature,
            String light,
            String humidity,
            String pressure
    ){
        this.light = light;
        this.humidity = humidity;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public String getTemperature(){
        return this.temperature;
    }
    public String getHumidity(){
        return this.humidity;
    }
    public String getLight(){
        return this.light;
    }
    public String getPressure(){
        return this.pressure;
    }
    public void setTemperature(String temperature){
        this.temperature = temperature;
    }
    public void setHumidity(String humidity){
        this.humidity = humidity;
    }
    public void setLight(String light){
        this.light = light;
    }
    public void setPressure(String pressure){
        this.pressure = pressure;
    }
}
