package com.badboy.mongodb;


import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.mongodb.operation.OrderBy;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Map;

public class MyMongodbTest {

    public static void main(String[] args) {



       /* MongoClient mongo = new MongoClient();
        MongoDatabase db = mongo.getDatabase("testDB");
        MongoCollection table = db.getCollection("message");

        Document document = new Document();
        document.put("name", "aaaa");
        document.put("code", 123456);
        document.put("oneMonth", 0);
        document.put("threeMonth", 0);
        document.put("sixMonth", 0);
        document.put("oneYear", 0);
        document.put("threeYear", 0);

        table.insertOne(document);*/


        MongoClient client = new MongoClient();
        MongoDatabase test = client.getDatabase("testDB");
        MongoCollection<Document> table = test.getCollection("message");
        System.out.println(table.count());
        //table.drop();

        //BasicDBObject oneYear = new BasicDBObject("oneYear", -1);
        // "oneYear", "threeYear","oneMonth", "threeMonth", "sixMonth"
        Bson descending = Sorts.descending("oneMonth");

        Document document = new Document("oneMonth", new Document("$gt", 3))
                                    .append("threeMonth",new Document("$lt",5))
                                    .append("sixMonth", new Document("$gt", 0))
                                    .append("oneYear", new Document("$gt", 0))
                                    .append("threeYear", new Document("$gt", 0));

        FindIterable<Document> documents = table.find(document).sort(descending).limit(100);

        documents.forEach(new Block<Document>() {
            public void apply(Document document) {
                System.out.println(document);
            }
        });









    }
}
