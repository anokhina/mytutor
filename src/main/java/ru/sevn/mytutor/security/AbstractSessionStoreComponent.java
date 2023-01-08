package ru.sevn.mytutor.security;

import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import ru.sevn.util.CookiesUtil;

public class AbstractSessionStoreComponent<SESSION extends HasRememberMe> {
    private final SessionRepository<SESSION> sessionStoreEntityRepository;
    private final String cookieName;
    
    protected AbstractSessionStoreComponent(String cookieName, SessionRepository<SESSION> sessionStoreEntityRepository) {
        this.sessionStoreEntityRepository = sessionStoreEntityRepository;
        this.cookieName = cookieName;
    }

    public SESSION getSessionStoreEntity (final HttpServletRequest request) {
        var cookie = getSessionCookie (request);
        if (cookie != null) {
            return getSessionStoreEntity (cookie.getValue ());
        }
        else {
            return null;
        }
    }

    private SESSION getSessionStoreEntity (final String cookieValue) {
        if (cookieValue != null) {
            return sessionStoreEntityRepository.findById (cookieValue);
        }
        else {
            return null;
        }
    }

    private Cookie getSessionCookie (final HttpServletRequest request) {
        final Cookie cookie = getCookie (request);
        if (cookie == null || cookie.getValue ().trim ().isEmpty ()) {
            return null;
        }
        else {
            return cookie;
        }
    }

    private Cookie getCookie (final HttpServletRequest request) {
        return CookiesUtil.findFirst (request, cookieName).orElse (null);
    }

    public void setRememberMe (final SESSION sessionStoreEntity, final boolean force) {
        if (sessionStoreEntity.getRememberMe () != null || force) {
            sessionStoreEntity.setRememberMe (System.currentTimeMillis ());
            sessionStoreEntityRepository.save (sessionStoreEntity);
        }
    }

    public void delete (final SESSION sessionEntity) {
        sessionStoreEntityRepository.delete (sessionEntity);
    }

    private SESSION create (final String sessionId, final String login, final boolean remember) {
        return sessionStoreEntityRepository.createAndSave(sessionId, login, remember, System.currentTimeMillis ());
    }

    private Cookie getAnyCookie (final boolean createNew, final HttpServletRequest request) {
        final Cookie c = getCookie (request);

        if (c == null && createNew) {
            final Cookie cookie = new Cookie (cookieName, UUID.randomUUID ().toString ());

            cookie.setPath ("/");

            return cookie;
        }
        if (c != null && c.getPath () == null) {
            c.setPath ("/");
        }

        return c;
    }

    public void logoutRemember (final HttpServletRequest request, final HttpServletResponse response) {
        var sessionEntity = getSessionStoreEntity (request);
        if (sessionEntity != null) {
            delete (sessionEntity);
        }

        final var cookie = getAnyCookie (false, request);
        if (cookie != null) {
            cookie.setMaxAge (0);
            response.addCookie (cookie);
        }
        response.addCookie (cookie);
    }

    public void logout (HttpServletRequest request, HttpServletResponse response) {
        logoutRemember (request, response);

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler ();
        logoutHandler.logout (
                request, null,
                null);
    }

    public void loginRemember (HttpServletRequest request, HttpServletResponse response, final String login, final boolean remember) {
        var cookie = getAnyCookie (true, request);
        if (remember) {
            cookie.setMaxAge (1000000);
        }
        response.addCookie (cookie);
        create (cookie.getValue (), login, remember);
    }
    
}
