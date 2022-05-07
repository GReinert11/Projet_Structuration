package fr.ul.miage;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
//import org.bson.Document;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;


    public class HTTPTools {
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

      /*  public Document getTrackInfo(String artist) {
            try {
// Préparation de la requête
                String url = "http://ws.audioscrobbler.com/…" +
                        URLEncoder.encode(artist, "UTF-8");
                HTTPTools httpTools = new HTTPTools();
                String jsonResponse = httpTools.sendGet(url);
                Document docLastFm = Document.parse(jsonResponse);
// Création du JSON à retourner
                Document respDoc = new Document();
// Extraction de données de docLastFm et insertion dans respDoc
// voir l’API org.bson.Document
                return resPodc;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/

}

