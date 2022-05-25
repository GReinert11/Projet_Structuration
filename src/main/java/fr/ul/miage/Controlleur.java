package fr.ul.miage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controlleur {

    @FXML
    Button btn_valider;
    @FXML
    TextField txt_album;
    @FXML
    TextField txt_artiste;
    @FXML
    Label lbl_listeners;
    @FXML
    Label lbl_plays;
    @FXML
    Label lbl_summary;
    @FXML
    TextArea txt_summary;
    @FXML
    TextArea txt_similar;

    HTTPTools http = new HTTPTools();
    String key = "3ba4c69c0e050af5d80f980dd0864d3c";


    @FXML
    void validerSaisie() throws UnsupportedEncodingException{

        String artiste = txt_artiste.getText();
        String artisteWithoutSpace = checkSpace(artiste);
        String album = txt_album.getText();
        
        Document docArtiste = getArtistInfo(artiste,key);
        System.out.println(docArtiste);
        String summaryArtiste = (String) docArtiste.get("summary");
       // System.out.println(summaryArtiste);
        txt_summary.appendText(summaryArtiste);


        //System.out.println(docArtiste);
        List<String> lstats = HTTPTools.getStatsFromArtist(artiste, key);
      
        String nbListeners = lstats.get(0);
        String nbPlays = lstats.get(1);
        lbl_listeners.setText(nbListeners);
        lbl_plays.setText(nbPlays);
        String sum = "Test insert doc2";
        List<String> tags = new ArrayList<>();
        tags.add("Tag5");
        tags.add("Tag6");
        tags.add("Tag7");

   
        

    }
    public Document getArtistInfo(String artiste,String key){

      
      MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
      MongoDatabase database = mongoClient.getDatabase("miage");
      Document docTest = database.getCollection("GMJGR_auteurs").find(Filters.eq("name",artiste)).first();
      FindIterable<Document> iterable = database.getCollection("GMJGR_auteurs").find(new Document("name", artiste));
      System.out.println(docTest);
      if(iterable.first() != null){
        txt_similar.appendText("aaaa");
        return docTest;

      }else{
        String url = "https://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" +artiste +"&api_key=" + key + "&format=json";
        HTTPTools httpTools = new HTTPTools();
        String jsonResponse = httpTools.sendGet(url);
        Document infoArtist = (Document) Document.parse(jsonResponse).get("artist");
        Document summaryArtist = (Document) infoArtist.get("bio");
        String bioArtiste= (String) summaryArtist.get("summary");
        Document docSimilar = (Document) infoArtist.get("similar");
        List<Document> ldocsSimilar = (List<Document>) docSimilar.get("artist");
        for(Document doc : ldocsSimilar){
          txt_similar.appendText((String) doc.get("name") + "\n");

        }
        Document last = new Document();
        last.append("name", infoArtist.get("name"));
        last.append("similar", infoArtist.get("similar"));
        last.append("summary", bioArtiste);
        database.getCollection("GMJGR_auteurs").insertOne(last);
        return last;
        
      }
      
      
  }

  public String checkSpace(String artiste){
    String newString = "";
    if(artiste.contains(" ")){
      newString = artiste.replaceAll(" ","%20");
    }
    return newString;
  }
    
}
