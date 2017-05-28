package com.zygimantus.apiwebas.api;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.swapi.models.Film;
import com.swapi.models.SWModelList;
import java.io.IOException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zygimantus
 */
@RestController
@RequestMapping("api/swapi")
public class SwApiController extends ApiController<SWModelList> {

    @Autowired
    private SwApiConsumer swApiConsumer;

    @Override
    protected void init() {
    }

    @Override
    protected SWModelList page() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "films", method = GET)
    protected SWModelList<Film> films() throws InterruptedException, IOException {

        SWModelList<Film> modelList = swApiConsumer.getFilmsList();

//        ArrayList<Film> movies = modelList.results;
        // Connect to MongoDB Server on localhost, port 27017 (default)
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        // Connect to Database "cartoon"
        final MongoDatabase database = mongoClient.getDatabase("cartoon");
        System.out.println("Successful database connection established. \n");

        //Insert a document into the "characters" collection.
        MongoCollection<Document> collection = database.getCollection("characters");

        // Delete the collection and start fresh
        collection.drop();
        Document mickeyMouse = new Document();
        Document charlieBrown = new Document();

        mickeyMouse.append("_id", 1)
                .append("characterName", "Mickey Mouse")
                .append("creator", new Document("firstName", "Walt").append("lastName", "Disney"))
                .append("pet", "Goofy");

        charlieBrown.append("_id", 2)
                .append("characterName", "Charlie Brown")
                .append("creator", new Document("firstName", "Charles").append("lastName", "Shultz"))
                .append("pet", "Snoopy");

        try {
            collection.insertOne(mickeyMouse);
            collection.insertOne(charlieBrown);
            System.out.println("Successfully inserted documents. \n");
        } catch (MongoWriteException mwe) {
            if (mwe.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("Document with that id already exists");
            }
        }

        System.out.println("Done");

        return modelList;
    }

}
