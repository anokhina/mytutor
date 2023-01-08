package ru.sevn.mytutor.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.builders.LessonDocumentBuilder;
import ru.sevn.mytutor.entities.Lesson;

@Component
public class LessonDaoComponent extends DaoComponent<Lesson, LessonDocumentBuilder> {

    @Autowired
    private LessonDocumentBuilder documentBuilder;
    
    @Override
    public LessonDocumentBuilder getObjectDocumentBuilder() {
        return documentBuilder;
    }

    @Override
    public String getObjectId(Lesson obj) {
        return obj.getName();
    }

}
