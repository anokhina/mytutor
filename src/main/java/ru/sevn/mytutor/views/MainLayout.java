package ru.sevn.mytutor.views;


import com.couchbase.lite.CouchbaseLiteException;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasElement;
import ru.sevn.mytutor.components.appnav.AppNav;
import ru.sevn.mytutor.components.appnav.AppNavItem;
import ru.sevn.mytutor.views.about.AboutView;
import ru.sevn.mytutor.views.helloworld.HelloWorldView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sevn.mytutor.dao.CourseDaoComponent;
import ru.sevn.mytutor.dao.UserDaoComponent;
import ru.sevn.mytutor.entities.Course;
import ru.sevn.mytutor.security.SecurityService;
import ru.sevn.mytutor.security.SecurityUtil;
import ru.sevn.mytutor.views.admin.AdminView;
import ru.sevn.mytutor.views.course.CourseView;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private final SecurityService securityService;
    
    private final CourseDaoComponent courseDaoComponent;
    
    private final UserDaoComponent userDaoComponent;
    
    public MainLayout(
            SecurityService securityService,
            CourseDaoComponent courseDaoComponent,
            UserDaoComponent userDaoComponent
    ) {
        this.securityService = securityService;
        this.courseDaoComponent = courseDaoComponent;
        this.userDaoComponent = userDaoComponent;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        HorizontalLayout header = new HorizontalLayout(viewTitle, new Button("Log out", e -> securityService.logout()));
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(viewTitle); 
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");
        
        header.expand(viewTitle);
        addToNavbar(true, toggle, header);
    }

    private void addDrawerContent() {
        H1 appName = new H1("My Tutor");
        
        if (SecurityUtil.getUser() != null) {
            appName.setText(SecurityUtil.getUser().getUsername());
        }
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        //https://icons8.com/line-awesome
        if (SecurityUtil.hasAuthority("ROLE_ADMIN")) {
            nav.addItem(new AppNavItem(getTitle(HelloWorldView.class), HelloWorldView.class, "la la-globe"));
            nav.addItem(new AppNavItem(getTitle(AdminView.class), AdminView.class, "la la-user-shield"));
        }
        
        try {
            printCourses(nav);
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(MainLayout.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        nav.addItem(new AppNavItem(getTitle(AboutView.class), AboutView.class, "la la-file"));
        
        return nav;
    }
    
    private String getTitle(Class<?> cls) {
        PageTitle title = cls.getAnnotation(PageTitle.class);
        return title == null ? cls.getSimpleName() : title.value();
    }
    
    private void printCourses(AppNav nav) throws CouchbaseLiteException {
        Consumer<Course> fn = (el) -> {
            nav.addItem(makeAppNavItemCourse(el));
        };
        if (SecurityUtil.hasAuthority("ROLE_ADMIN")) {
            courseDaoComponent.findObjects().forEach(fn);
        } else {
            var appuser = SecurityUtil.getUser();
            var user = appuser.getUser();

            courseDaoComponent.findOwnObjects(user).forEach(fn);
            courseDaoComponent.findInObjects(user).forEach(fn);
        }
    }
    
    private <CMP extends Component> AppNavItem makeAppNavItemCourse(Course el) {
        return makeAppNavItem(el.getId(), el.getName(), CourseView.class, "la la-globe");
    }
    
    private <CMP extends Component> AppNavItem makeAppNavItem(String id, String name, Class<CMP> navigationTarget, String iconClass) {
        final RouteParameters parameters = new RouteParameters (
                new RouteParam ("id", "" + id));
        final var router = VaadinService.getCurrent ().getRouter ();
        String url = RouteConfiguration.forRegistry (router.getRegistry ()).getUrl (navigationTarget, parameters);
        return new AppNavItem(name, url, iconClass);
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        //viewTitle.setText(getCurrentPageTitle());
    }

    private Registration titleUpdateListener;
    
    @Override
    public void setContent(Component content) {
        if (titleUpdateListener != null) {
            titleUpdateListener.remove();
            titleUpdateListener = null;
        }
        super.setContent(content);
        if (content instanceof HasTitle) {
            titleUpdateListener = ((HasTitle)content).addComponentTitleChangedListener(evt -> {
                updateTitle();
            });
        }
        updateTitle();
    }
    
    private void updateTitle() {
        viewTitle.setText("" + getCurrentPageTitle());
    }
    
    private String getCurrentPageTitle() {
        if (getContent() instanceof HasTitle) {
            return ((HasTitle)getContent()).getPageTitle();
        }
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
