package fr.ul.miage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.util.JSON;

import org.bson.Document;
import org.bson.json.*;
import org.json.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import netscape.javascript.JSObject;

public class Album {

    private String name;
    private String artist;
    private int id_album;
    private int listeners;
    private int playcount;
    public Album(String name, String artist, int listeners, int playcount) {
        this.name = name;
        this.artist = artist;
        this.listeners = listeners;
        this.playcount = playcount;
    }

    public Album(String artist, String type) {
        this.artist = artist;
        this.type = type;
    }

    private String type;
    public Album(){

    }

   


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static JSONObject getAlbum(HTTPTools h, String key, String artiste, String album) throws JSONException{

        String url = "https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key="+ key +"&artist="+artiste+"&album="+album+"&format=json";
        String res = h.sendGet(url);
        JSONObject jsonObj = new JSONObject(res);
        String al = jsonObj.getString("album");
        JSONObject obj = new JSONObject(al);
        String getArtiste = obj.getString("artist");
        int listener = obj.getInt("listeners");
        //System.out.println(listener);
    
        

        //Document doc = Document.parse(obj.getString("artist"));
        //System.out.println(doc);
        

        
        return jsonObj;


    }

    public String getArtist(JsonElement jElt){
        return jElt
        .getAsJsonObject()
        .get("artist")
        .getAsString();
    }

    public static void accesJson(JSONObject json){
        try {
            //Object invalid = json.get("invalid");
            String summary = json.getString("summary");
            System.out.println(summary);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
