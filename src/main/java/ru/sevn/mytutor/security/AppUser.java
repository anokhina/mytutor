package ru.sevn.mytutor.security;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
public class AppUser extends User {
    
    private final ru.sevn.mytutor.entities.User user;
    
    public AppUser(ru.sevn.mytutor.entities.User u) {
        super(u.getLogin(), "{noop}userpss", Collections.singleton(new SimpleGrantedAuthority("ROLE_" + u.getUserType().name())));
        this.user = u;
    }

    public ru.sevn.mytutor.entities.User getUser() {
        return user;
    }

}
