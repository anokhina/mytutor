package ru.sevn.mytutor.dao;

import com.couchbase.lite.CouchbaseLiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.entities.User;
import ru.sevn.mytutor.builders.UserDocumentBuilder;

@Component
public class UserDaoComponent extends DaoComponent<User, UserDocumentBuilder> {
    
    @Autowired
    private UserDocumentBuilder documentBuilder;
    
    public User findUser(String login) throws CouchbaseLiteException {
        User id = new User();
        id.setLogin(login);
        return findObject(id);
    }
    
    public User addUserOnNotFound(User obj) throws CouchbaseLiteException {
        return addOnNotFound(obj);
    }

    @Override
    public UserDocumentBuilder getObjectDocumentBuilder() {
        return documentBuilder;
    }

    @Override
    public String getObjectId(User obj) {
        return obj.getLogin();
    }
    
}
