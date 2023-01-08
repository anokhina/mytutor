package ru.sevn.mytutor.dao;

import com.couchbase.lite.CouchbaseLiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.builders.SessionDocumentBuilder;
import ru.sevn.mytutor.entities.Session;

@Component
public class SessionDaoComponent extends DaoComponent<Session, SessionDocumentBuilder> {
    
    @Autowired
    private SessionDocumentBuilder documentBuilder;
    
    public Session findSession(String sessionId) throws CouchbaseLiteException {
        return findObject(Session.builder().sessionId(sessionId).login("FAKE").build());
    }
    
    @Override
    public SessionDocumentBuilder getObjectDocumentBuilder() {
        return documentBuilder;
    }

    @Override
    public String getObjectId(Session obj) {
        return obj.getSessionId();
    }
    
}
