package fr.ul.miage;

import java.util.Iterator;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;

import javafx.scene.control.TextField;

public class Utilisateur{

public Utilisateur(String username, String password) {
        this.username = username;
        this.password = password;
    }
private String username;
private String password;
private String role;
public Utilisateur(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
}
public String getUsername() {
    return username;
}
public void setUsername(String username) {
    this.username = username;
}
public String getPassword() {
    return password;
}
public void setPassword(String password) {
    this.password = password;
}
public String getRole() {
    return role;
}
public void setRole(String role) {
    this.role = role;
}


public Utilisateur(){
    
}
public static boolean checkIfUserExists(String username){
    MongoDatabase database = HTTPTools.connectionToDatabase();
    FindIterable<Document> iterable = database.getCollection("GMJGR_users").find(new Document("username", username));
    Iterator iterator = iterable.iterator();
    if(iterator.hasNext()){
      return true;
    }else{
      return false;
    }

  }

  public static void inscription(String username, String password, String role){
    MongoDatabase database = HTTPTools.connectionToDatabase();
    Document docInscription = new Document();
    docInscription.append("username",username);
    docInscription.append("password",password);
    docInscription.append("role",role);

    try {
        database.getCollection("GMJGR_users").insertOne(docInscription);
    } catch (Exception e) {
        e.printStackTrace();
    }
   

  }

  public static boolean checkRoleEntry(String role){
      if (role.equals("user") || role.equals("moderateur") || role.equals("administrateur")){
          return true;
      }else{
          return false;
      }
  }


}

