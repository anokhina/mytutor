package ru.sevn.mytutor.security;

import com.couchbase.lite.CouchbaseLiteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.dao.SessionDaoComponent;
import ru.sevn.mytutor.entities.Session;

@Component
public class SessionRepositoryComponent implements SessionRepository<Session>{

    @Autowired
    private SessionDaoComponent sessionDaoComponent;
    
    @Override
    public Session findById(String cookieValue) {
        try {
            return sessionDaoComponent.findSession(cookieValue);
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(SessionRepositoryComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void delete(Session s) {
        try {
            sessionDaoComponent.delete(s);
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(SessionRepositoryComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Session create(String sessionId, String login) {
        return Session.builder().login(login).sessionId(sessionId).build();
    }

    @Override
    public Session save(Session s) {
        try {
            return sessionDaoComponent.addOnNotFound(s);
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(SessionRepositoryComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
