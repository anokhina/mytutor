package ru.sevn.mytutor.db;

import org.springframework.stereotype.Component;
import com.couchbase.lite.CouchbaseLiteException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sevn.mytutor.entities.*;
import ru.sevn.mytutor.dao.*;
import ru.sevn.mytutor.entities.UserType;

@Component
public class DbComponent {
    
    @Autowired
    private UserDaoComponent userDaoComponent;
    
    @Autowired
    private CourseDaoComponent courseDaoComponent;
    
    @Autowired
    private LessonDaoComponent lessonDaoComponent;
    
    private void initData() throws CouchbaseLiteException {
        var admin = userDaoComponent.addUserOnNotFound(
            User.builder()
            .login("admin")
            .name("admin")
            .userType(UserType.ADMIN)
            .build()
        );
        
        var misha = userDaoComponent.addUserOnNotFound(
            User.builder()
            .login("misha")
            .name("Миша")
            .userType(UserType.STUDENT)
            .build()
        );
        var danila = userDaoComponent.addUserOnNotFound(
            User.builder()
            .login("danila")
            .name("Данила")
            .userType(UserType.STUDENT)
            .build()
        );
        var tutor = userDaoComponent.addUserOnNotFound(
            User.builder()
            .login("nika")
            .name("Ника")
            .userType(UserType.TUTOR)
            .build()
        );
        
        var courseM = courseDaoComponent.addOnNotFound(
            Course.builder()
                    .name("Физика Миша")
                    .tutor(tutor)
                    .students(Set.of(misha))
                    .build()
        );
        var courseD = courseDaoComponent.addOnNotFound(
            Course.builder()
                    .name("Физика Данила")
                    .tutor(tutor)
                    .students(Set.of(danila))
                    .build()
        );
        
        lessonDaoComponent.addOnNotFound(
                Lesson.builder()
                        .course(courseM)
                        .name("1. Введение")
                        .date(LocalDate.of(2022, 12, 24).atStartOfDay().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli())
                        .fullName("Введение в предмет")
                        .homeAssignment("Прочитать стр")
                .build()
        );
        
        lessonDaoComponent.addOnNotFound(
                Lesson.builder()
                        .course(courseD)
                        .name("1. Введение")
                        .date(LocalDate.of(2022, 12, 24).atStartOfDay().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli())
                        .fullName("Введение в предмет")
                        .homeAssignment("Прочитать стр")
                .build()
        );
    }
    
    @PostConstruct
    void init() {
        try {
            initData();
        } catch (CouchbaseLiteException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
//https://docs.couchbase.com/couchbase-lite/current/java/document.html