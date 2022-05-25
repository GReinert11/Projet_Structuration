package fr.ul.miage;

import org.bson.BasicBSONObject;
import org.bson.BsonReader;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.*;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.util.JSON;

//import org.bson.Document;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;


    public class HTTPTools {
        private static MongoDatabase database = connectionToDatabase();
        // temps minimum d'une requête HTTP en ms (4 seconds)
        private int mt = 4000;

        // dernière requête HTTP
        private long last;


        public String sendGet(String url) {
            // vérifie le temps écoulé depuis la requête précédente
            while (System.currentTimeMillis() - last < mt) ;
            last = System.currentTimeMillis();

            try {
                // préparation de la requête
                StringBuilder result = new StringBuilder();
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF8");
                BufferedReader br = new BufferedReader(isr);

                // obtention de la réponse
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }

                // fermeture du lecteur et retour
                br.close();
                isr.close();
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        
       
        public static Document getTrackInfo(String artiste,String album,String key) throws UnsupportedEncodingException{
            Document docLastFm = new Document();
            MongoDatabase database = connectionToDatabase();
            String testCollectionAuthors = "GMJGR_auteurs";
            MongoCollection<Document> collectionAuthors = database.getCollection(testCollectionAuthors);
            // Préparation de la requête
            String url = "https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key="+ key +"&artist="+artiste+"&album="+album+"&format=json";
            HTTPTools httpTools = new HTTPTools();
            String jsonResponse = httpTools.sendGet(url);
            //System.out.println(jsonResponse);
            docLastFm = Document.parse(jsonResponse);
            // System.out.println(docLastFm);
            System.out.println(docLastFm.size());
            for(int i = 0; i < docLastFm.size(); i++){
                
                System.out.println(docLastFm.get(1).getClass());
                System.out.println("a");
            }

            String json = com.mongodb.util.JSON.serialize(docLastFm);
        
           // System.out.println( docLastFm.get("artist",String.class));
            //System.out.println(docLastFm.getString("artist"));

           
            // System.out.println(docLastFm.get("artist"));
            // System.out.println(docLastFm.getString("artist"));
            Document respDoc = new Document();

            List results = new ArrayList<>();
            //collection.find(docLastFm).into(results);
                    

            // Création du JSON à retourner
                            //Document respDoc = new Document();
            
            // Extraction de données de docLastFm et insertion dans respDoc
            // voir l’API org.bson.Document
                            ;
            return docLastFm;
            
        }

        public static MongoDatabase connectionToDatabase(){
            MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            MongoDatabase database = mongoClient.getDatabase("miage");
            
            // DB database = mongoClient.getDB("miage");
            return database;

        }

        /*public static void insertIntoDatabase(){
            MongoDatabase database = connectionToDatabase();
             //Creer Collection
            DBCollection linked=database.createCollection("GMJGR_auteurs",new BasicDBObject()); 
            System.out.println("Collection created successfully");

            //Selectionner collection
            DBCollection CollectionAuteur = database.getCollection("GMJGR_auteurs");

            //Insertion des données
            BasicDBObject doc = new BasicDBObject();
            doc.append("name", "name_auteurs");
            doc.append("artist", "name_artist" );

            CollectionAuteur.insert(doc);
        }*/

        public static void createArtistDocument(String name, String summary,List<String>tags){

            MongoDatabase database = connectionToDatabase();
            String testCollectionAuthors = "GMJGR_auteurs";
            MongoCollection<Document> collectionAuthors = database.getCollection(testCollectionAuthors);
            boolean collectionAuthorsExists = collectionAuthors.count() > 0 ? true : false;
            Document doc = new Document();
            doc.append("name",name);
            doc.append("summary",summary);
            List<BasicDBObject> listTags = new ArrayList<>();
            BasicDBList testListTags = new BasicDBList();
            Map<String,String> documentMap = new HashMap<String,String>();

            BasicDBObjectBuilder dbBuiler = BasicDBObjectBuilder.start();
            for(int i = 0; i < tags.size(); i++){
                //doc.append("tags", tags.get(i));
               // listTags.add(new BasicDBObject("tags",tags.get(i)));
               // testListTags.add(new BasicDBObject("tags",tags.get(i)));
                documentMap.put("tag",tags.get(i));
                dbBuiler.add("tag", tags.get(i));
            }
            doc.append("tags", dbBuiler.get());

            System.out.println(documentMap.toString());
           // doc.append(key, value)
           // doc.append("tag", "uwu");

            
            

            database.getCollection(testCollectionAuthors).insertOne(doc);
           
             





        }

        public static List<String> getStatsFromArtist(String name,String key){
            Document infoArtist = new Document();
            List<String> lstats = new ArrayList<>();
            String url = "https://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=Cher&api_key=" + key + "&format=json";
            HTTPTools httpTools = new HTTPTools();
            String jsonResponse = httpTools.sendGet(url);
            infoArtist = Document.parse(jsonResponse);
            //System.out.println(infoArtist);

            JsonWriterSettings settings = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
            //System.out.println(infoArtist.toJson(settings));
            
            String obj = infoArtist.toJson(settings);
            try {
                JSONObject obj2 = new JSONObject(obj);
                JSONObject obj3 = (JSONObject) obj2.get("artist");
                JSONObject obj4 = (JSONObject) obj3.get("stats");
                String nbListeners = (String) obj4.get("listeners");
                String nbPlaycount = (String) obj4.get("playcount");
                lstats.add(nbListeners);
                lstats.add(nbPlaycount);
                System.out.println(nbListeners);
                System.out.println(nbPlaycount);
            }catch(Exception e){
                e.printStackTrace();
            }
            return lstats;
        }

        public static void collectionAlbums(Document d){
            database.createCollection("GMJGR_albums");
            //MongoCollection<Document> collectionAlbums = database.getCollection("GMJGR_albums");
            //database.getCollection("GMJGR_albums").insertOne(d);



        }

        public static List<Document> testGetInfoAuthor(String nameAuthor){
            MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
           /* DB db = mongoClient.getDB("miage");
            DBCollection dbcoll = db.getCollection("GMJGR_auteurs");
            BasicDBObject searchQuery = new BasicDBObject();
            
            searchQuery.put("surname", "test2");
            DBCursor cursor = dbcoll.find(searchQuery);
            while (cursor.hasNext())
            {
	        System.out.println(cursor.next());
            }
            */

            List<Document> res = new ArrayList<>();
            MongoDatabase database = mongoClient.getDatabase("miage");
            MongoCollection<Document> collection = database.getCollection("GMJGR_auteurs");
            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put("name", nameAuthor);
            System.out.println(collection.find(whereQuery));
           
            FindIterable<Document> cursor2 = collection.find(whereQuery);
            MongoCursor<Document> iterator = cursor2.iterator();
            while (iterator.hasNext()) {
                System.out.println("ok");
                res.add(iterator.next());
               // System.out.println(iterator.next());
            }

            return res;


        }
        
        public static Document getTopArtists(String key){
            
            String url = "https://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&api_key="+ key + "&format=json";
            HTTPTools httpTools = new HTTPTools();
            String jsonResponse = httpTools.sendGet(url);
            Document infoArtist = (Document) Document.parse(jsonResponse).get("artists");
            List<Document> doc2 = (List<Document>) infoArtist.get("artist");
            List<String> arNames = new ArrayList<>();
            List<String> arPlaycount = new ArrayList<>();
            List<String> arListeners = new ArrayList<>();
            List<Artiste> lartists = new ArrayList<>();
            List<Integer> arPlaycountTest = new ArrayList<>();
           // System.out.println(doc2);
            for(Document doc : doc2){
                //Artiste ar = new Artiste((String) doc.getString("name"), (String) doc.getString("playcount"), (String) doc.getString("listeners"));
                //System.out.println(ar);
                //lartists.add(ar);
               // System.out.println((String) doc.getString("listeners"));
                //System.out.println((String) doc.getString("playcount"));
                String name = (String) doc.get("name");
             

                String playcount = (String) doc.get("playcount");
                String listeners = (String) doc.get("listeners");
                
                
                //String listeners = (String) doc.getString("listeners");
                //Artiste arTest = new Artiste(listeners);
               // System.out.println(arTest);
                arNames.add(name);
                arPlaycount.add(playcount);
                arListeners.add(listeners);
                
            }
            //System.out.println(names.toString());
            List<String> namesTen = new ArrayList<>();
            List<String> playcountTen = new ArrayList<>();
            List<String> listenersTen = new ArrayList<>();
            List<Integer> playCountTest = new ArrayList<>();
            for(int i = 0 ; i <= 10; i++){
                namesTen.add(arNames.get(i));
                playcountTen.add(arPlaycount.get(i));
                listenersTen.add(arListeners.get(i));
                //System.out.println(playCountTest);
                Artiste ar = new Artiste(namesTen.get(i), playcountTen.get(i),listenersTen.get(i));
                //System.out.println(ar);
                lartists.add(ar);
            }
           // System.out.println(namesTen);
            //System.out.println(playcountTen);
            //System.out.println(listenersTen);
            //System.out.println(lartists);
            //System.out.println(onlyTopTen);
            //System.out.println(lartists);

            Document lastDoc = new Document();
            //lastDoc.append("name", name);
    
            
            
            return lastDoc;

        }

        public static JSONObject getTopTracks(String key){
            Document doc = new Document();
            String url = "https://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key="+ key + "&format=json";
            HTTPTools httpTools = new HTTPTools();
            String jsonResponse = httpTools.sendGet(url);
            doc = Document.parse(jsonResponse);

            JsonWriterSettings settings = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
            //System.out.println(infoArtist.toJson(settings));
            JSONObject obj2 = null;
            JSONObject obj3 = null;
            JSONObject obj4 = null;
            String obj = doc.toJson(settings);
           // System.out.println(obj);
            try {
                obj2 = new JSONObject(obj);
                obj3 = (JSONObject) obj2.get("tracks");
                obj4 = (JSONObject) obj3.get("@attr");
                //System.out.println(obj3);

            } catch (Exception e) {
                //TODO: handle exception
            }


            return obj4;




        }

        public static Document getTopTags(String key){
            Document doc = new Document();
            String url = "https://ws.audioscrobbler.com/2.0/?method=chart.gettoptags&api_key="+ key + "&format=json";
            HTTPTools httpTools = new HTTPTools();
            String jsonResponse = httpTools.sendGet(url);
            doc = Document.parse(jsonResponse);
            return doc;

        }

}

