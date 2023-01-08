package ru.sevn.mytutor.security;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static <T> T getPrincipal () {
        if (hasUserDetailsAuthentiction ()) {
            return (T) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
        }
        else {
            return null;
        }
    }

    public static AppUser getUser () {
        return getPrincipal ();
    }

    public static UserDetails getUserDetails () {
        return getPrincipal ();
    }

    public static boolean hasUserDetailsAuthentiction () {
        return SecurityContextHolder.getContext () != null && SecurityContextHolder.getContext ().getAuthentication () != null && SecurityContextHolder.getContext ().getAuthentication ().getPrincipal () != null
                && SecurityContextHolder.getContext ().getAuthentication ().getPrincipal () instanceof UserDetails;
    }

    public static boolean hasAuthority (final String authority) {
        if (SecurityContextHolder.getContext () != null &&
                SecurityContextHolder.getContext ().getAuthentication () != null &&
                SecurityContextHolder.getContext ().getAuthentication ().getAuthorities () != null) {

            for (final GrantedAuthority grantedAuthority : SecurityContextHolder.getContext ().getAuthentication ().getAuthorities ()) {
                if (grantedAuthority.getAuthority ().equals (authority)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasAuthority (final UserDetails ud, final String authority) {
        if (ud != null) {
            for (final GrantedAuthority grantedAuthority : ud.getAuthorities ()) {
                if (grantedAuthority.getAuthority ().equals (authority)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasAuthorities (final String... authorities) {
        if (SecurityContextHolder.getContext () != null &&
                SecurityContextHolder.getContext ().getAuthentication () != null &&
                SecurityContextHolder.getContext ().getAuthentication ().getAuthorities () != null) {

            for (final String authority : authorities) {
                for (final GrantedAuthority grantedAuthority : SecurityContextHolder.getContext ().getAuthentication ().getAuthorities ()) {
                    if (grantedAuthority.getAuthority ().equals (authority)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasAuthorities (final UserDetails ud, final String... authorities) {
        if (ud != null) {
            for (final String authority : authorities) {
                for (final GrantedAuthority grantedAuthority : ud.getAuthorities ()) {
                    if (grantedAuthority.getAuthority ().equals (authority)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasAllAuthorities (final UserDetails ud, final String... authorities) {
        if (ud != null && authorities.length > 0) {
            for (final String authority : authorities) {
                if (! hasAuthority (ud, authority)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static Collection<String> getAuthorities () {
        final Set<String> result = new TreeSet<> ();
        if (SecurityContextHolder.getContext () != null &&
                SecurityContextHolder.getContext ().getAuthentication () != null &&
                SecurityContextHolder.getContext ().getAuthentication ().getAuthorities () != null) {

            for (final GrantedAuthority grantedAuthority : SecurityContextHolder.getContext ().getAuthentication ().getAuthorities ()) {
                result.add (grantedAuthority.getAuthority ());
            }
        }
        return result;
    }

    public static <TR extends Throwable> void runSecured (ThrowableRunnable<TR> runSecured, String... roles) throws TR {
        runSecuredElse (runSecured, null, roles);
    }

    public static <TR extends Throwable> void runSecuredElse (ThrowableRunnable<TR> runSecured, ThrowableRunnable<TR> runElse, String... roles) throws TR {
        if (hasAuthorities (roles)) {
            runSecured.run ();
        }
        else if (runElse != null) {
            runElse.run ();
        }
    }

    public interface ThrowableRunnable<EXCEPTION extends Throwable> {
        void run () throws EXCEPTION;
    }    
}
