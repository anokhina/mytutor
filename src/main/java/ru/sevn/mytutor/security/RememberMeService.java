package ru.sevn.mytutor.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.sevn.util.CookiesUtil;

@Service
public class RememberMeService implements org.springframework.security.web.authentication.RememberMeServices {
    public static final String REMEMBER_ME_KEY = "rememberMeKey";
    public static final String REMEMBER_ME_USE = "rememberMeUse";
    
    @Autowired
    private SessionStoreComponent sessionStoreComponent;
    
    @Autowired
    private LoginService loginService;
    
    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        var sessionEntity = sessionStoreComponent.getSessionStoreEntity (request);
        if (sessionEntity != null) {
            try {
                var login = sessionEntity.getLogin ();
                org.springframework.security.core.userdetails.User user = loginService.login (login, true);
                if (sessionEntity.getRememberMe () == null) {
                    throw new BadCredentialsException ("Remove session");
                }
                sessionStoreComponent.setRememberMe (sessionEntity, false);
                return new RememberMeAuthenticationToken (REMEMBER_ME_KEY, user, user.getAuthorities ());
            }
            catch (final BadCredentialsException ex) {
                sessionStoreComponent.delete (sessionEntity);
                return null;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {
        var sessionEntity = sessionStoreComponent.getSessionStoreEntity (request);
        if (sessionEntity != null) {
            sessionStoreComponent.delete (sessionEntity);
        }
    }

    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        sessionStoreComponent.loginRemember (request, response, getLogin (successfulAuthentication), CookiesUtil.getBooleanValue (request, REMEMBER_ME_USE));
    }

    private String getLogin (Authentication successfulAuthentication) {
        var ud = getUserDetails (successfulAuthentication);
        return ud.getUsername ();
    }
    private UserDetails getUserDetails (Authentication successfulAuthentication) {
        return (UserDetails) successfulAuthentication.getPrincipal ();
    }
}
