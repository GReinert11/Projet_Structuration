package fr.ul.miage;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClients;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    
    public static void main(String[] args) {
 
        HTTPTools http = new HTTPTools();
        Album al = new Album();
        //String test = http.sendGet("https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=3ba4c69c0e050af5d80f980dd0864d3c&artist=Cher&album=Believe&format=json");
        String key = "3ba4c69c0e050af5d80f980dd0864d3c";
        // String album = al.getAlbum(http,key);
        Artiste a = al.getAlbum(http,key);
        System.out.println(a.toString());
        //System.out.println(album);
       // System.out.println(test);
        System.out.println("a");

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("admin");
        //mongoClient.getDatabaseNames().forEach(System.out::println);

        String connectionString = "mongodb://localhost:27017";
        try (MongoClient mongoCl = new MongoClient(new MongoClientURI("mongodb://localhost:27017"))) {
            List<Document> databases = mongoCl.listDatabases().into(new ArrayList<>());
            databases.forEach(db -> System.out.println(db.toJson()));
        }

        // boolean auth = database.authenticate("username", "pwd".toCharArray());
        //MongoClient mg = new MongoClient();
    }

    
}
