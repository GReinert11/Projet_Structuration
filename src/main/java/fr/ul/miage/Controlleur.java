package fr.ul.miage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.naming.directory.SearchControls;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCallback;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import org.bson.BasicBSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class Controlleur {


    //Buttons
    @FXML
    Button btn_valider;
    @FXML 
    Button btn_valider2;
    @FXML
    Button btn_valider3;
    @FXML
    Button btn_valider4;
    @FXML
    Button btn_inscription;
    @FXML
    Button btn_connect;
    @FXML
    Button btn_validerAvis;
    @FXML
    Button btn_afficherAvis;

    //Labels
    @FXML
    Label lbl_listeners;
    @FXML
    Label lbl_plays;
    @FXML
    Label lbl_summary;
    @FXML
    Label lbl_name2;
    @FXML
    Label lbl_artist2;
    @FXML
    Label lbl_release2;
    @FXML
    Label lbl_playcount2;
    @FXML
    Label lbl_listeners2;
   

    @FXML
    TabPane tabPane;

    //TextField
    @FXML
    TextField txt_artiste;
    @FXML
    TextField txt_artiste2;
    @FXML
    TextField txt_artiste4;
    @FXML
    TextField txt_album2;
    @FXML
    TextField txt_album4;
    @FXML
    TextField txt_chanson4;
    @FXML
    TextField txt_note4;
    @FXML
    TextField txt_user;
    @FXML
    TextField txt_password;
    @FXML
    TextField txt_username2;
    @FXML
    TextField txt_password2;
    @FXML
    TextField txt_role;
    @FXML
    TextField txt_country;
    @FXML
    TextField txt_albumavis;
   
   
   
  


    //TextArea
    @FXML
    TextArea txt_summary;
    @FXML
    TextArea txt_similar;
    @FXML
    TextArea txt_tags;
    @FXML
    TextArea txt_tracks2;
    @FXML
    TextArea txt_tags2;
    @FXML
    TextArea txt_summary2;
    @FXML
    TextArea txt_artiste3;
    @FXML
    TextArea txt_commentaire4;
    @FXML
    TextArea txt_topcountry;
    @FXML
    TextArea txt_topcountry2;
    @FXML
    TextArea txt_validerAvis;

    //Tab
    @FXML
    Tab tab_artists;
    @FXML
    Tab tab_albums;
    @FXML
    Tab tab_topten;
    @FXML
    Tab tab_recommendations;
    @FXML
    Tab tab_avis;
    @FXML
    Tab tab_administration;


    Utilisateur user;
    HTTPTools http = new HTTPTools();
    String key = "3ba4c69c0e050af5d80f980dd0864d3c";


    @FXML
    void validerSaisie() throws UnsupportedEncodingException{
        resetControls();
        String artiste = txt_artiste.getText();
        String artisteWithoutSpace = checkSpace(artiste);        
        Document docArtiste = getArtistInfo(artiste,key);

        //System.out.println(docArtiste);
       
      
   
        

    }

    //Recupérer toutes les informations sur un artistes
    public Document getArtistInfo(String artiste,String key){
     
      //Statistiques sur l'artiste
      List<String> lstats = HTTPTools.getStatsFromArtist(artiste, key);
      String nbListeners = lstats.get(0);
      String nbPlays = lstats.get(1);
      lbl_listeners.setText(nbListeners);
      lbl_plays.setText(nbPlays);

      
      MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
      MongoDatabase database = mongoClient.getDatabase("miage");
      Document docTest = database.getCollection("GMJGR_auteurs").find(Filters.eq("name",artiste)).first();
      FindIterable<Document> iterable = database.getCollection("GMJGR_auteurs").find(new Document("name", artiste));
      Iterator iterator = iterable.iterator();
      int count = 0;
      //System.out.println(docTest);
      if(iterator.hasNext()){ //Vérification si le document existe déjà dans la base de données
        count = 1;
        txt_summary.appendText((String) docTest.get("summary"));
        Document docArtistsSimilar = (Document) docTest.get("similar");
        Document tags = (Document) docTest.get("tags");
        System.out.println(tags);
        List<Document> ltags = (List<Document>) tags.get("tag");
        List<Document> lartists = (List<Document>) docArtistsSimilar.get("artist");
        for(Document doc : lartists){
          txt_similar.appendText((String) doc.get("name") + "\n");
        }
        for(Document doc : ltags){
          txt_tags.appendText((String) doc.get("name") + "\n");
        }
        return docTest;

      }else{ //Si il n'existe pas, on va chercher les informations sur l'api et on insère le document dans la base de données
        String artisteWithoutSpace = checkSpace(artiste);
        String url = "https://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" +artisteWithoutSpace +"&api_key=" + key + "&format=json";
        HTTPTools httpTools = new HTTPTools();
        String jsonResponse = httpTools.sendGet(url);
        Document infoArtist = (Document) Document.parse(jsonResponse).get("artist");
        Document summaryArtist = (Document) infoArtist.get("bio");
        String bioArtiste= (String) summaryArtist.get("summary");
        Document docSimilar = (Document) infoArtist.get("similar");
        Document tags = (Document) infoArtist.get("tags");
        List<Document> ldocsSimilar = (List<Document>) docSimilar.get("artist");
        List<Document> ltags = (List<Document>) tags.get("tag");
        txt_summary.appendText((String) bioArtiste);
        for(Document doc : ldocsSimilar){
          txt_similar.appendText((String) doc.get("name") + "\n");
        }
         for(Document doc : ltags){
          txt_tags.appendText((String) doc.get("name") + "\n");
        }
        Document last = new Document();
        last.append("name", infoArtist.get("name"));
        last.append("similar", infoArtist.get("similar"));
        last.append("summary", bioArtiste);
        last.append("tags",infoArtist.get("tags"));
        database.getCollection("GMJGR_auteurs").insertOne(last); //Ajout du nouveau document dans la base
        return last;
        
      }
      
      
  }

  public  Document getTrackInfo(String artiste,String album,String key) throws UnsupportedEncodingException{
    Document docLastFm = new Document();
    MongoDatabase database = HTTPTools.connectionToDatabase();
   
    //database.createCollection("GMJGR_albums");
    Document respDoc = new Document();
    List<Bson> filters = new ArrayList<>();
    filters.add(Filters.eq("name", album));
    filters.add(Filters.eq("artist", artiste));
    Document documents = (Document) database.getCollection("GMJGR_albums").find(Filters.eq("name",album)).first();
    FindIterable<Document> iterable = database.getCollection("GMJGR_albums").find(new Document("name", album));
   
    //System.out.println(documents);
    int count = 0;
    Iterator iterator = iterable.iterator();
    if(iterator.hasNext()){
      count = 1;
      lbl_name2.setText((String) documents.get("name"));
      lbl_artist2.setText((String) documents.get("artist"));
      lbl_release2.setText((String) documents.get("reeased"));
      lbl_listeners2.setText((String) documents.get("listeners"));
      lbl_playcount2.setText((String) documents.get("playcount"));
      txt_summary2.appendText((String) documents.get("summary"));
      Document docTracks = (Document) documents.get("tracks");
    
      List<Document> lTracks = (List<Document>) docTracks.get("track");
      for(Document doc : lTracks){
        Document rank = (Document) doc.get("@attr");
        txt_tracks2.appendText((String) doc.get("name") + ", rank : " + (int) rank.get("rank")  + "\n");
      }
      Document docTags = (Document) documents.get("tags");
      List<Document> lTags = (List<Document>) docTags.get("tag");
      for(Document doc : lTags){
        txt_tags2.appendText((String) doc.get("name") + "\n");
      }
      return documents;

    }else{
      Document docTest = database.getCollection("GMJGR_albums").find(Filters.eq("name",album)).first();
      // Document d = database.getCollection("GMJGR_albums").find(filter);
       Bson filter1 = Filters.eq("name", album);
       Bson filter2 = Filters.eq("artist",artiste);
       FindIterable<Document> iterable2 = database.getCollection("GMJGR_auteurs").find(new Document("name", artiste));
    
       // Préparation de la requête
       String url = "https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key="+ key +"&artist="+artiste+"&album="+album+"&format=json";
       HTTPTools httpTools = new HTTPTools();
       String jsonResponse = httpTools.sendGet(url);
       Document infoAlbum = (Document) Document.parse(jsonResponse).get("album");
    
       String nameAlbum = (String) infoAlbum.get("name");
       lbl_name2.setText(nameAlbum);
       String artistAlbum = (String) infoAlbum.get("artist");
       lbl_artist2.setText(artistAlbum);
       Document docWiki = (Document) infoAlbum.get("wiki");
       String listeners = (String) infoAlbum.get("listeners");
       lbl_listeners2.setText(listeners);
       String playcount = (String) infoAlbum.get("playcount");
       lbl_playcount2.setText(playcount);
       System.out.println(docWiki);
       String releaseAlbum = (String) docWiki.get("published");
       lbl_release2.setText(releaseAlbum);
       String summary = (String) docWiki.get("summary");
       txt_summary2.appendText(summary);

       Document tags = (Document) infoAlbum.get("tags");
       List<Document> ltags = (List<Document>) tags.get("tag");
       for(Document doc : ltags){
         txt_tags2.appendText((String) doc.get("name") + "\n");
       }

       Document tracks = (Document) infoAlbum.get("tracks");
       List<Document> ltracks = (List<Document>) tracks.get("track");
       for(Document doc : ltracks){
         Document rank = (Document) doc.get("@attr");
         String getNameAlbum = (String) doc.get("name");
         //int getDurationAlbum = (int) doc.get("duration");
         int getRank = (int) rank.get("rank");
         String duration = "null";
         /*if((doc.get("duration") != null))
         {
            txt_tracks2.appendText(getNameAlbum + ", durée : " + getDurationAlbum + ", rank : " + Integer.toString(getRank) + "\n" );
         }else{
          txt_tracks2.appendText(getNameAlbum + ", durée : " + duration + "secondes, rank :" + Integer.toString(getRank) + "\n" );
         }*/

         txt_tracks2.appendText((String) doc.get("name") + ", rank : " + (int) rank.get("rank")  + "\n");
       }

       Document last = new Document();
       last.append("name", nameAlbum);
       last.append("artist", artistAlbum);
       last.append("released", releaseAlbum);
       last.append("summary", summary);
       last.append("tracks", tracks);
       last.append("listeners", listeners);
       last.append("playcount",playcount);
       last.append("tags", tags);
       database.getCollection("GMJGR_albums").insertOne(last); //Ajout du nouveau document dans la base
     

       return docLastFm;
      }

    }

    public Utilisateur inscription(String username, String password){
      Document doc = new Document();
      Utilisateur user = new Utilisateur();

      return user;

    }

    public boolean connexion(String username, String password){

      MongoDatabase db = HTTPTools.connectionToDatabase();
      MongoCollection<Document> dbCollection = db.getCollection("GMJGR_users");
      boolean userExists = Utilisateur.checkIfUserExists(username);
      if(userExists){
        Document documents = (Document) db.getCollection("GMJGR_users").find(Filters.eq("pseudo",username)).first();
        String role = (String) documents.get("role");
  
      }
  
  
  
      return true;
  }
    @FXML
    void topArtistsCountry(){
      String country = txt_country.getText();
      Document docCountry = getTopByCountry(country);

          
  }

    public Document getTopByCountry(String country){
      Document returnDoc = new Document();
      String url = "https://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&country=" + country + "&limit=10&api_key="+ key + "&format=json";
      MongoDatabase db = HTTPTools.connectionToDatabase();
      MongoCollection dbCollection = db.getCollection("GMJGR_topArtistsByCountry");

      try {
        Document docToFind = (Document) dbCollection.find(Filters.eq("country",country)).first();
        FindIterable<Document> iterable = db.getCollection("GMJGR_topArtistsByCountry").find(new Document("country", country));
        Iterator iterator = iterable.iterator();
        if(iterator.hasNext()){

        }else{

          HTTPTools httpTools = new HTTPTools();
          String jsonResponse = httpTools.sendGet(url);
          Document infoArtist = (Document) Document.parse(jsonResponse).get("topartists");
          returnDoc.append("country",country);
          //Document docArtists = (Document) infoArtist.get("artist");
         
          List<Document> lDocArtists = (List<Document>) infoArtist.get("artist");
          System.out.println(lDocArtists);
          for(Document doc : lDocArtists){  
            Document listDocReturn = new Document();
            String name = (String) doc.get("name");
            String listeners = (String) doc.get("listeners");
            listDocReturn.append("country",country);
            listDocReturn.append("name",name);
            listDocReturn.append("listeners",listeners);
            db.getCollection("GMJGR_topArtistsByCountry").insertOne(listDocReturn);
          }
          

        }
       
      
      } catch (Exception e) {
        
      }







      return returnDoc;
    }
  


                    
   
    
    public Document getTopArtists(String key,String country){
      Document returnDoc = new Document();
      String url = "https://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&limit=10&api_key="+ key + "&format=json";
      MongoDatabase db = HTTPTools.connectionToDatabase();
      MongoCollection dbCollection = db.getCollection("GMJGR_topArtists");
      Document docTest = (Document) db.getCollection("GMJGR_topArtists").find();
      if(docTest != null){
        System.out.println("yes");
      }else{
        System.out.println("nope");
      }
      HTTPTools httpTools = new HTTPTools();
      String jsonResponse = httpTools.sendGet(url);
      Document infoArtist = (Document) Document.parse(jsonResponse).get("artists");
      List<Document> listeDocs = (List<Document>) infoArtist.get("artist");
      for(int i = 0; i <= 10; i++){
        String name = (String) listeDocs.get(i).get("name");
        String playcount = (String) listeDocs.get(i).get("playcount");
        String listeners = (String) listeDocs.get(i).get("listeners");
        txt_artiste3.appendText(name + ", playcount : " + playcount + ", listeners : " + listeners + "\n");
        Document docToInsert = new Document();
        docToInsert.append("name",name);
        docToInsert.append("playcount",playcount);
        docToInsert.append("listeners",listeners);
        db.getCollection("GMJGR_topArtists").insertOne(docToInsert);

      }
      /*for(Document doc : listeDocs){
          String name =  (String) doc.get("name");
          System.out.println(name);
          //String playcount = (String) listeDocs.get(i).get("playcount");
         // System.out.println(playcount);
          //String listeners = (String) listeDocs.get(i).get("listeners");
          txt_artiste3.appendText(", name : " + name ); //", listeners : " + listeners);
      }*/
      return returnDoc;
    }
        

@FXML
  void saisieAlbums(){
    String artiste = txt_artiste2.getText();
    String album = txt_album2.getText();
    try {
      Document docAlbums = getTrackInfo(artiste, album, key);
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String checkSpace(String artiste){
    String newString = "";
    if(artiste.contains(" ")){
      newString = artiste.replaceAll(" ","%20");
    }
    return newString;
  }

  public void resetControls(){ //Reset les contrôles pour que l'utilisateur puisse lancer plusieurs fois une recherche 
    lbl_listeners.setText("");
    lbl_plays.setText("");
    lbl_summary.setText("");
    txt_summary.setText("");
    txt_similar.setText("");
  }

  @FXML
  void lookTopArtists(){
    //getTopArtists(key);

  }

  public boolean checkIfAlbumExists(String album){
    MongoDatabase database = HTTPTools.connectionToDatabase();
    Document documents = (Document) database.getCollection("GMJGR_albums").find(Filters.eq("name",album)).first();
    FindIterable<Document> iterable = database.getCollection("GMJGR_albums").find(new Document("name", album));
    Iterator iterator = iterable.iterator();
    if(iterator.hasNext()){
      return true;
    }else{
      return false;
    }

  }

  @FXML
  void inscription(){
    String username = txt_username2.getText();
    String password = txt_password2.getText();
    String role = txt_role.getText();
    boolean verifRole = Utilisateur.checkRoleEntry(txt_role.getText());
    if(!verifRole){
      Alert alert = new Alert(AlertType.WARNING);
		  alert.setTitle("Inscription");
		  alert.setContentText("Saisir un role valide ! (user/moderateur/adminstrateur");
		  alert.showAndWait();
    }else{
      Utilisateur.inscription(username, password, role);
     // tabPane.getSelectionModel().selectNext();
    }

  }
  @FXML
  void connection(){
    MongoDatabase database = HTTPTools.connectionToDatabase();
    String username = txt_user.getText();
    String password = txt_password.getText();
    boolean userExist = Utilisateur.checkIfUserExists(username);
    if(userExist){
      Document doc = (Document) database.getCollection("GMJGR_users").find(Filters.eq("username",username)).first();
      String role = (String) doc.get("role");
      System.out.println(role);
      switch(role){
        case "user":
          tab_albums.setDisable(false);
          tab_artists.setDisable(false);
          tab_topten.setDisable(false);
          tab_recommendations.setDisable(false);
          tab_avis.setDisable(true);
          tab_administration.setDisable(true);
        break;

        case "moderateur":
          tab_albums.setDisable(false);
          tab_artists.setDisable(false);
          tab_topten.setDisable(false);
          tab_recommendations.setDisable(false);
          tab_avis.setDisable(false);
          tab_administration.setDisable(true);
        break;
        
        case "administrateur":
          tab_albums.setDisable(false);
          tab_artists.setDisable(false);
          tab_topten.setDisable(false);
          tab_recommendations.setDisable(false);
          tab_avis.setDisable(false);
          tab_administration.setDisable(false);
        break;
      }
      user = new Utilisateur(username, password,role);
     
    }
    

    }

    public void resetControls2(){
      txt_artiste4.setText("");
      txt_album4.setText("");
      txt_commentaire4.setText("");
      txt_note4.setText("");

    }

    public Document addCommentAlbum(String artist,String album,String comment,String username, int rate){
      Document docToinsert = new Document();
      MongoDatabase database = HTTPTools.connectionToDatabase();
      MongoCollection<Document> dbCollection = database.getCollection("GMJGR_avisAlbums");
      boolean docExist = checkIfAlbumExists(album);

     // Document checkExistComment = (Document) database.getCollection("GMJGR_avisAlbums").find(Filters.eq("name",album)).first();
      FindIterable<Document> checkExistComment = (FindIterable<Document>) database.getCollection("GMJGR_avisAlbums").find(Filters.and(Filters.eq("username",user.getUsername()),Filters.eq("name",album)));
      //indIterable<Document> iterable2
      System.out.println(checkExistComment);
    /*  if(checkExistComment != null){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Avis");
        alert.setContentText("Vous avez déjà saisi un avis pour cet album");
       // resetControls2();
        checkExistComment.
        alert.showAndWait();
      }else{*/
        docToinsert.append("artist",artist);
        docToinsert.append("name",album);
        docToinsert.append("comment",comment);
        docToinsert.append("rate",rate);
        docToinsert.append("username",username);
        dbCollection.insertOne(docToinsert);
        System.out.println("non");
      





     /* try {
        Document checkExistComment = (Document) database.getCollection("GMJGR_avisAlbums").find(Filters.eq("album",album)).first();
      //  Document checkExistComment = (Document) database.getCollection("GMJGR_avisAlbums").find(Filters.and(Filters.eq("username",user.getUsername()),Filters.eq("name",album)));
        String albumCheck = (String) checkExistComment.get("album");
        String usernameCheck = (String) checkExistComment.get("username");
        if(albumCheck.contains(album) && usernameCheck.contains(username)){
          Alert alert = new Alert(AlertType.WARNING);
		      alert.setTitle("Avis");
		      alert.setContentText("Vous avez déjà saisi un avis pour cet album");
		      alert.showAndWait();
          System.out.println("ok");
        }
      
        /*if(checkExistComment != null){
          Alert alert = new Alert(AlertType.WARNING);
		      alert.setTitle("Avis");
		      alert.setContentText("Vous avez déjà saisi un avis pour cet album");
		      alert.showAndWait();
          System.out.println("ok");
        }else{
          System.out.println("ok2");
        
        }
      } catch (Exception e) {
        docToinsert.append("artist",artist);
        docToinsert.append("name",album);
        docToinsert.append("comment",comment);
        docToinsert.append("rate",rate);
        docToinsert.append("username",username);
        dbCollection.insertOne(docToinsert);
        System.out.println("pas bon");
      }*/
      return docToinsert;

    }
  

    public List<Document> getAvis(String album){
      List<Document> ldocs = new ArrayList();
      String a = "a";
      MongoDatabase database = HTTPTools.connectionToDatabase();
      MongoCollection<Document> dbCollection = database.getCollection("GMJGR_avisAlbums");
      //FindIterable<Document> iterable = (FindIterable<Document>) database.getCollection("GMJGR_avisAlbums");
     // MongoCursor<Document> cursor2 = dbCollection.find(Filters.and(Filters.eq("verif", 0),Filters.eq("album"),album)).iterator();
      //FindIterable<Document> cursor = dbCollection.find();
      MongoCursor<Document> cursor = dbCollection.find(Filters.and(Filters.eq("verif",0), Filters.eq("album", album))).iterator();
      while(cursor.hasNext()){
        System.out.println("yes");
      }
      
      /*Iterator iterator = cursor2.iterator();
      while((iterator.hasNext())){
        System.out.println(iterator.next());
        iterator.next().getString("_id");
        String comment = iterator.next().getString("comment");
        mapAvis.put((int)((BasicBSONObject) iterator).get("_id"), (String) ((BasicBSONObject) iterator).get("comment"));
        mapAvis.put(iterator.get("_id"));
      }*/
      //System.out.println(mapAvis);
     
     
      //Document group = new Document("$group", new Document("_id", "$type").append("number", new Document("$count", new Document())));
      //Document test = (Document) dbCollection.find().projection(Projections.include("id","comment"));


      
      

      return ldocs;
    }

  @FXML
  void afficherAvis(){
    String album = txt_albumavis.getText();
    getAvis(album);
  }
  

  @FXML
  void validerAvis(){
    

  }

  @FXML
  void recommendations(){

    String artist = txt_artiste4.getText();
    String album = txt_album4.getText();
    String comment = txt_commentaire4.getText();
    int rate = Integer.parseInt(txt_note4.getText());
    String username = user.getUsername();

    Document docAvis = addCommentAlbum(artist, album, comment, username,rate);
    
    
    /*MongoDatabase database = HTTPTools.connectionToDatabase();
    MongoCollection<Document> dbCollection = database.getCollection("GMJGR_albums");
    String artist = txt_artiste4.getText();
    String album = txt_album4.getText();
    String comment = txt_commentaire4.getText();
    System.out.println(comment);
    int rate = Integer.parseInt(txt_note4.getText());
    boolean docExist = checkIfAlbumExists(album);
    if(docExist){
      System.out.println("yes");
      try {
        Document doc = database.getCollection("GMJGR_albums").find(new Document("name", album)).first();
       // doc.append("comment",comment);
       Document query = new Document().append("rate",rate);
       Bson updates = Updates.combine(Updates.set("rate",rate));
       BasicDBObject searchQuery = new BasicDBObject("name", album);
       UpdateOptions options = new UpdateOptions().upsert(true);
       doc.append("rate",rate);
     //  UpdateResult result = dbCollection.updateOne(searchQuery, updates);
       
       addFieldWithValueToDoc(album,rate,comment);
        /*
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("comment", comment);
        updateFields.append("rate",rate);
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);
        UpdateResult updateQueryResult = dbCollection.updateMany(searchQuery,setQuery);*/
       
    
        //Updates.combine(Updates.set("department_id", 3), Updates.set("job", "Sales Manager")));
        
     // } catch (Exception e) {
        //TODO: handle exception
     // }
    

    //}else{
      //System.out.println("nope");
   // }
    /*try {
      Document doc = getTrackInfo(artist, album, key);
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }*/
  }

  private void addFieldWithValueToDoc(String album, int rate, String comment) {
    MongoDatabase database = HTTPTools.connectionToDatabase();
    boolean verif = false;
    MongoCollection<Document> dbCollection = database.getCollection("GMJGR_albums");
    UpdateResult updateQueryResult = dbCollection.updateMany(Filters.eq("name", album),
    Updates.combine(Updates.set("rate", rate),Updates.set("comment", comment),Updates.set("verif", verif )));
  }

  

  /*public void addFieldWithValueToDoc(String album, String docID, String key, String value){
    MongoDatabase database = HTTPTools.connectionToDatabase();
    MongoCollection<Document> dbCollection = database.getCollection("GMJGR_albums");
    dbCollection.updateOne(new BasicDBObject("name", album),
    new BasicDBObject("$set", new BasicDBObject(key, value)));
}*/
    
}
