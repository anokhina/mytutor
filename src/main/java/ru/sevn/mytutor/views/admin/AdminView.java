package ru.sevn.mytutor.views.admin;

import ru.sevn.mytutor.views.about.*;
import com.couchbase.lite.CouchbaseLiteException;
import com.vaadin.flow.component.grid.Grid;
import ru.sevn.mytutor.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sevn.mytutor.builders.ObjectDocumentBuilder;
import ru.sevn.mytutor.db.DbComponent;
import ru.sevn.mytutor.dao.*;
import ru.sevn.mytutor.entities.*;

@PermitAll
@PageTitle("Admin")
@Route(value = "admin", layout = MainLayout.class)
public class AdminView extends VerticalLayout {
    
    @Autowired
    public AdminView(
            CourseAttachementsDaoComponent courseAttachementsDaoComponent,
            SessionDaoComponent sessionDaoComponent,
            UserDaoComponent userDaoComponent,
            CourseDaoComponent courseDaoComponent,
            LessonDaoComponent lessonDaoComponent
    ) {
        setSpacing(false);
        setSizeFull();
        
        addGrid("Sessions", Session.class, sessionDaoComponent);
        addGrid("Users", User.class, userDaoComponent);
        addGrid("Courses", Course.class, courseDaoComponent);
        addGrid("Lessons", Lesson.class, lessonDaoComponent);
        addGrid("CourseAttachements", CourseAttachements.class, courseAttachementsDaoComponent);
    }
    
    private <OBJ extends BaseObject, BLDR extends ObjectDocumentBuilder<OBJ>> Grid<OBJ> addGrid(String label, Class<OBJ> cls, DaoComponent<OBJ, BLDR> dc) {
        Grid<OBJ> grid = new Grid<>(cls, true);
        add(new Label(label), grid);
        try {
            var items = dc.findObjects();
            grid.setItems(items);
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
        }
        grid.getColumns().forEach(c -> c.setResizable(true));
        return grid;
    }
}
