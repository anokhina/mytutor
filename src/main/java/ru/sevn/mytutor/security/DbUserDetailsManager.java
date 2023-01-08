package ru.sevn.mytutor.security;

import com.couchbase.lite.CouchbaseLiteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.sevn.mytutor.dao.UserDaoComponent;

public class DbUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

    @Autowired
    private UserDaoComponent userDaoComponent;
    
    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean userExists(String username) {
        try {
            var user = userDaoComponent.findUser(username);
            return user != null;
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(DbUserDetailsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var user = userDaoComponent.findUser(username);
            if(user != null) {
                return new AppUser(user);
            }
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(DbUserDetailsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return user;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
