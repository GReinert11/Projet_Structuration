package fr.ul.miage;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controlleur {

    @FXML
    Button btn_valider;
    @FXML
    TextField txt_album;
    @FXML
    TextField txt_artiste;
    HTTPTools http = new HTTPTools();
    String key = "3ba4c69c0e050af5d80f980dd0864d3c";


    @FXML
    void validerSaisie(){

        String artiste = txt_artiste.getText();
        String album = txt_album.getText();

        try {
            // String getInfoAlbum = Album.getAlbum(http, key, artiste,album);
            JSONObject test = Album.getAlbum(http, key, artiste, album);
            System.out.println(test.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }
    
}
