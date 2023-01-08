package ru.sevn.mytutor.views.course;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Ordering;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sevn.mytutor.dao.*;
import ru.sevn.mytutor.entities.Course;
import ru.sevn.mytutor.entities.CourseAttachements;
import ru.sevn.mytutor.entities.Lesson;
import ru.sevn.mytutor.entities.LessonAttachements;
import ru.sevn.mytutor.security.SecurityUtil;
import ru.sevn.mytutor.views.HasTitle;
import ru.sevn.mytutor.views.MainLayout;
import ru.sevn.util.DateUtil;
import ru.sevn.va.dialog.VaMessageDialog;

//course attachements
//lesson attachements
@PermitAll
@Route(value = "course/:id", layout = MainLayout.class)
public class CourseView extends VerticalLayout implements BeforeEnterObserver, HasTitle/*, HasDynamicTitle*/ {

    private LessonGrid grid;
    
    private Grid<CourseAttachements> gridCourseAttachements = new Grid(CourseAttachements.class, true);
        
    private String title = "";
    
    private Label label = new Label(title);
    
    private Button newAttachButton = new Button (VaadinIcon.PLUS.create ());
    private Button newButton = new Button (VaadinIcon.PLUS.create ());
            
    @Autowired
    private CourseDaoComponent courseDaoComponent;
    
    @Autowired
    private LessonDaoComponent lessonDaoComponent;
    
    @Autowired
    private CourseAttachementsDaoComponent courseAttachementsDaoComponent;
    
    private AttachementPanel<CourseAttachements> attachPanel = new AttachementPanel();
    private VaMessageDialog<AttachementPanel<CourseAttachements>> attachDialog;
    
    public CourseView() {
        attachDialog = VaMessageDialog.getMessageDialogYN("Upload", attachPanel, () -> {
            if (attachPanel.getBlobObject().getData() != null) {
                try {
                    courseAttachementsDaoComponent.save(attachPanel.getBlobObject());
                    refreshAttachements();
                    return true;
                } catch (CouchbaseLiteException ex) {
                    Logger.getLogger(CourseView.class.getName()).log(Level.SEVERE, null, ex);
                    Notification.show("Ошибка сохранения");
                }
            }
            return false;
        });

        add(newAttachButton);
        newAttachButton.addClickListener(evt -> {
            if (course != null) {
                var bo = CourseAttachements.builder()
                        .course(course)
                        .build();
                attachPanel.setBlobObject(bo);
                attachDialog.open();
            }
        });
        add(gridCourseAttachements);
        gridCourseAttachements.setAllRowsVisible(true);
        grid = new LessonGrid(lesson -> {
            var user = SecurityUtil.getUser().getUser();
            
            if (SecurityUtil.hasAuthority("ROLE_ADMIN") ||
                    lesson.getCourse().getTutor().equals(user)
                    ) {
                
                try {
                    var dbLesson = lessonDaoComponent.findObject(lesson);
                    if (lesson.getId() == null) {
                        if (dbLesson == null) {
                            lessonDaoComponent.addOnNotFound(lesson);
                            return OperationResult.added();
                        } else {
                            return OperationResult.error("уже существует");
                        }
                    } else {
                        if (dbLesson != null && !lesson.getId().equals(dbLesson.getId())) {
                            return OperationResult.error("уже существует");
                        } else {
                            lessonDaoComponent.save(lesson);
                            return OperationResult.modified();
                        }
                    }
                } catch (CouchbaseLiteException ex) {
                    Logger.getLogger(CourseView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                return OperationResult.error("нет прав");
            }
            return OperationResult.error("");
        }, (lesson, result) -> {
            switch (result.getResultType()) {
                case ADDED:
                case DELETED:
                case MODIFIED:
                    refreshLessons();
                    break;
                default:
            }
        });
        add(label, newButton, grid);
        newButton.addClickListener(e -> {
            if (course != null) {
                var newLesson = Lesson.builder()
                        .date(System.currentTimeMillis())
                        .course(course)
                        .name("Урок " + DateUtil.today())
                        .build();
                grid.getEditorPanel().getBinder().setBean(newLesson);
                grid.getEditorDialog().open();
            }
        });
        grid.getColumns().forEach(c -> c.setResizable(true));
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        final var idStr = event.getRouteParameters ().get ("id").orElse(null);
        setValueId(idStr);
    }

    @Override
    public String getPageTitle() {
        return title;
    }
    
    private void setValueId(String id) {
        var dbObj = courseDaoComponent.findById(id);
        setValue(dbObj);
    }

    private void setValue(Course value) {
        this.course = null;
        grid.setItems();
        if (value == null) {
            setTitle("");
        } else {
            var isAdmin = SecurityUtil.hasAuthority("ROLE_ADMIN");
            var user = SecurityUtil.getUser().getUser();
            if (isAdmin || value.getTutor().equals(user)) {
                setAllowedValue(value);
            } else if (value.getStudents().contains(user)) {
                setAllowedValue(value);
            } else {
                setTitle("");
            }
        }
    }
    
    private Course course;
    
    private void setAllowedValue(Course value) {
        this.course = value;
        refresh();
        setTitle(value.getName());
        label.setText(title + ". " + value.getTutor().getName());
    }
    
    private void refresh() {
        refreshLessons();
        refreshAttachements();
    }
    
    private void refreshLessons() {
        if (course != null) {
            try {
                grid.setItems(lessonDaoComponent.findObjects(wh -> {
                    return wh.and(Expression.property("course").equalTo(Expression.string(course.getId())));
                }, Ordering.property("date").descending()));
            } catch (CouchbaseLiteException ex) {
                Logger.getLogger(CourseView.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            grid.setItems();
        }
    }
    
    private void refreshAttachements() {
        if (course != null) {
            try {
                gridCourseAttachements.setItems(courseAttachementsDaoComponent.findObjects(wh -> {
                    return wh.and(Expression.property("course").equalTo(Expression.string(course.getId())));
                }, Ordering.property("description").ascending()));
            } catch (CouchbaseLiteException ex) {
                Logger.getLogger(CourseView.class.getName()).log(Level.SEVERE, null, ex);
            }
            //gridCourseAttachements
        } else {
            gridCourseAttachements.setItems();
        }
    }
    
    private void setTitle(String s) {
        title = s;
        label.setText(title);
        fireComponentTitleChangedEvent();
    }
}
