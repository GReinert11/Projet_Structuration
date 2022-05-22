package fr.ul.miage.Projet_Structuration;



import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.json.JSONException;

import fr.ul.miage.Album;
import fr.ul.miage.Artiste;
import fr.ul.miage.Controlleur;
import fr.ul.miage.HTTPTools;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    @Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Projet structuration de document");

		try {
			//Parent root = FXMLLoader.load(getClass().getResource("a.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Interface.fxml"));
			Parent root2 = loader.load();
			Scene scene = new Scene(root2,1600,950);
			Controlleur ctrl = loader.getController();
			primaryStage.setScene(scene);
			primaryStage.show();

			//ctrl.setData();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


    
    public static void main(String[] args){
        launch(args);
 
       /* HTTPTools http = new HTTPTools();
        Album al = new Album();
        String test = http.sendGet("https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=3ba4c69c0e050af5d80f980dd0864d3c&artist=Cher&album=Believe&format=json");
        String key = "3ba4c69c0e050af5d80f980dd0864d3c";
        // String album = al.getAlbum(http,key);
        Artiste artiste = new Artiste();
        String test2 = "";
    
        System.out.println(test2);
        
        // artiste = al.getAlbum(http,key);m
        // Artiste test2 = new Artiste(test);
        // System.out.println(test.toString());
        // Arrays.stream(persons).forEach(System.out::println);
        //System.out.println(album);
       // System.out.println(test);
        System.out.println("a");*/

      /*  MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("admin");
        //mongoClient.getDatabaseNames().forEach(System.out::println);

        String connectionString = "mongodb://localhost:27017";
        try (MongoClient mongoCl = new MongoClient(new MongoClientURI("mongodb://localhost:27017"))) {
            List<Document> databases = mongoCl.listDatabases().into(new ArrayList<>());
            //databases.forEach(db -> System.out.println(db.toJson()));
        }

        // boolean auth = database.authenticate("username", "pwd".toCharArray());
        //MongoClient mg = new MongoClient(); */
    }

    
}
