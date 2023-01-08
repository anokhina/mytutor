package ru.sevn.mytutor.db;

import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class DbConnector {
    public static final String DB_NAME = "mydb";

    private Database database;

    public Database getDatabase() {
        return database;
    }

    @PostConstruct
    void init() {
        CouchbaseLite.init();
        
        System.out.println("Starting DB");
        DatabaseConfiguration cfg = new DatabaseConfiguration();
        try {
            database = new Database(  DB_NAME, cfg);
        } catch (CouchbaseLiteException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @PreDestroy
    void end() {
        try {
            database.close();
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(DbComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
