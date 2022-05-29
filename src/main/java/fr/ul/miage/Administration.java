package fr.ul.miage;

import java.time.LocalDate;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class Administration {

    private Document doc;
    private MongoDatabase base;
    private MongoCollection<Document> dbCollection;
    public Administration(MongoDatabase base, MongoCollection<Document> dbCollection) {
        this.base = base;
        this.dbCollection = dbCollection;
    }

    public Document getDoc() {
        return doc;
    }
    public void setDoc(Document doc) {
        this.doc = doc;
    }
    public MongoDatabase getBase() {
        return base;
    }
    public void setBase(MongoDatabase base) {
        this.base = base;
    }
    public MongoCollection<Document> getCollection() {
        return dbCollection;
    }
    public void setCollection(MongoCollection<Document> collection) {
        this.dbCollection = collection;
    }

    public void addRequestInBase(String typeRequest, boolean fromApi, String username, LocalDate date){
        Document docToAdd = new Document();
        docToAdd.append("typeRequest", typeRequest);
        docToAdd.append("username",username);
        docToAdd.append("date",date);
        if(fromApi){
            docToAdd.append("fromApi",true);     
        }else{
            docToAdd.append("fromApi",false);

        }
        dbCollection.insertOne(docToAdd);
        

    }
    
    
}
