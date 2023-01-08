package ru.sevn.mytutor.views.about;

import com.couchbase.lite.CouchbaseLiteException;
import ru.sevn.mytutor.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sevn.mytutor.db.DbComponent;
import ru.sevn.mytutor.dao.UserDaoComponent;

@PermitAll
@PageTitle("О приложении")
@Route(value = "about", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    @Autowired
    public AboutView(UserDaoComponent dbComponent) {
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);
        
        add(new H2("Приложение для домашки 🤗 "));
        add(new Paragraph("Здесь можно посмотреть задание и прикрепить выполненное "));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
    
}
