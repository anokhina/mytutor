package ru.sevn.mytutor.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.builders.LessonAttachementsBuilder;
import ru.sevn.mytutor.entities.LessonAttachements;

@Component
public class LessonAttachementsDaoComponent extends DaoComponent<LessonAttachements, LessonAttachementsBuilder> {

    @Autowired
    private LessonAttachementsBuilder documentBuilder;

    @Override
    public LessonAttachementsBuilder getObjectDocumentBuilder() {
        return documentBuilder;
    }

    @Override
    public String getObjectId(LessonAttachements obj) {
        return obj.getId();
    }
    
}
