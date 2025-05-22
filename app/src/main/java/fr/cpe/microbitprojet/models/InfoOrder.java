package fr.cpe.microbitprojet.models;

public class InfoOrder {
    private String name;
    private String id;
    private int imageId;

    public InfoOrder(String name, String id, int imageId){
        this.name = name;
        this.id = id;
        this.imageId = imageId;
    }

    public String getName(){
        return this.name;
    }

    public String getId(){
        return this.id;
    }

    public int getImageId(){
        return this.imageId;
    }
}
