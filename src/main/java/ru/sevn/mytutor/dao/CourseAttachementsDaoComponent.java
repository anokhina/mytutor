package ru.sevn.mytutor.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.builders.CourseAttachementsBuilder;
import ru.sevn.mytutor.entities.CourseAttachements;

@Component
public class CourseAttachementsDaoComponent extends DaoComponent<CourseAttachements, CourseAttachementsBuilder>{
    
    @Autowired
    private CourseAttachementsBuilder documentBuilder;

    @Override
    public CourseAttachementsBuilder getObjectDocumentBuilder() {
        return documentBuilder;
    }

    @Override
    public String getObjectId(CourseAttachements obj) {
        return obj.getId();
    }

}
