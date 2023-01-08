package ru.sevn.mytutor.security;

import com.couchbase.lite.CouchbaseLiteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.dao.UserDaoComponent;

@Component
public class LoginService {

    @Autowired
    private UserDaoComponent userDaoComponent;
    
    public User login(String login, boolean failOnBlocked) {
        try {
            var user = userDaoComponent.findUser(login);
            if(user != null) {
                return new AppUser(user);
            }
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(DbUserDetailsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
