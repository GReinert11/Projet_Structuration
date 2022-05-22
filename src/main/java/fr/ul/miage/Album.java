package fr.ul.miage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.util.JSON;
import org.bson.json.*;
import org.json.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Album {

    private String name;
    private String artist;
    private int id_album;
    

   


    public static JSONObject getAlbum(HTTPTools h, String key, String artiste, String album) throws JSONException{

        String url = "https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key="+ key +"&artist="+artiste+"&album="+album+"&format=json";
        String res = h.sendGet(url);
       // JsonParser parser = new JsonParser();
       // JsonElement jsonElt = parser.parse(res);
        // System.out.println(jsonElt);
        // String artist = getArtist(jsonElt);


        // String jsonString = res;
        Gson g = new Gson();
        GsonBuilder gbuilder = new GsonBuilder();
        Gson g2 = gbuilder.create();
        JSONObject jsonObj = new JSONObject(res);
        // System.out.println(jsonObj.toString());
        // System.out.println(jsonObj.getString("artist"));
        // sString res_test = jsonObj.getString("summary");
        // Artiste artiste1 = g2.fromJson(res, Artiste.class);
        // Artiste a = g.fromJson("{\"artist\": typeOfT)
        


        return jsonObj;


    }

    public String getArtist(JsonElement jElt){
        return jElt
        .getAsJsonObject()
        .get("artist")
        .getAsString();
    }
    
}
