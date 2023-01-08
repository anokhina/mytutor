package ru.sevn.mytutor.security;

import org.springframework.stereotype.Component;
import ru.sevn.mytutor.entities.Session;

@Component
public class SessionStoreComponent extends AbstractSessionStoreComponent<Session> {
    public static final String COOKIE_NAME = "mytutor";
    
    public SessionStoreComponent(SessionRepositoryComponent sessionRepositoryComponent) {
        super(COOKIE_NAME, sessionRepositoryComponent);
    }
}
