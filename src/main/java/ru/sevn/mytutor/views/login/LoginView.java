package ru.sevn.mytutor.views.login;

import ru.sevn.util.CookiesUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import javax.annotation.security.PermitAll;
import ru.sevn.mytutor.security.RememberMeService;

@PermitAll
@Route("login")
@PageTitle("Login | My Tutor")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    private final Checkbox rememberMe = new Checkbox (" Запомнить меня");
    
    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");

        rememberMe.addValueChangeListener (evt -> {
            final String key = RememberMeService.REMEMBER_ME_USE;
            final boolean value = rememberMe.getValue ();
            UI.getCurrent ().getPage ().executeJs ("document.cookie='" + key + "=" + value + "; path=/'");
        });
        rememberMe.setValue (CookiesUtil.getBooleanValue (VaadinServletRequest.getCurrent ().getHttpServletRequest (), RememberMeService.REMEMBER_ME_USE));
        
        add(new H1("My Tutor"), login, rememberMe);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
