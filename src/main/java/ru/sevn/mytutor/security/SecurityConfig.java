package ru.sevn.mytutor.security;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.sevn.mytutor.views.login.LoginView;

@EnableWebSecurity 
@Configuration
public class SecurityConfig extends VaadinWebSecurity { 

    @Autowired
    private RememberMeService rememberMeService;
    
    /**
     * Demo SimpleInMemoryUserDetailsManager, which only provides
     * two hardcoded in-memory users and their roles.
     * NOTE: This shouldn't be used in real-world applications.
     */
    private static class SimpleInMemoryUserDetailsManager extends InMemoryUserDetailsManager {
        public SimpleInMemoryUserDetailsManager() {
            createUser(new User("user",
                "{noop}userpass",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            ));
            createUser(new User("admin",
                "{noop}userpass",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ));
        }
    }
    

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/images/**").permitAll(); 

        super.configure(http);

        http
                .logout ().deleteCookies ("JSESSIONID", RememberMeService.REMEMBER_ME_KEY)
                .and()
                .rememberMe()
                .key(RememberMeService.REMEMBER_ME_KEY)
                .rememberMeServices(rememberMeService)
                //.tokenValiditySeconds(2*86400)
                ;
        
        setLoginView(http, LoginView.class); 
    }

    @Bean
    public DbUserDetailsManager userDetailsService() {
        return new DbUserDetailsManager(); 
    }
}
