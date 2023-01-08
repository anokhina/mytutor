package ru.sevn.mytutor.builders;

import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.dao.CourseDaoComponent;
import ru.sevn.mytutor.entities.Lesson;

@Component
public class LessonDocumentBuilder extends AbstractObjectDocumentBuilder<Lesson> {

    @Autowired
    private CourseDaoComponent courseDaoComponent;

    public LessonDocumentBuilder() {
        super(Lesson.class);
    }

    @Override
    public MutableDocument fillDocument(MutableDocument doc, Lesson ent) {
        doc.setString("name", ent.getName());
        doc.setString("course", ent.getCourse().getId());
        doc.setString("fullName", ent.getFullName());
        doc.setString("homeAssignment", ent.getHomeAssignment());
        doc.setLong("date", ent.getDate());
        return doc;
    }

    @Override
    public Lesson getObjectInstance(MutableDocument doc) {
        
        return Lesson.builder()
                .name(doc.getString("name"))
                .course(courseDaoComponent.findById(doc.getString("course")))
                .fullName(doc.getString("fullName"))
                .homeAssignment(doc.getString("homeAssignment"))
                .date(doc.getLong("date"))
                .build();
    }

    @Override
    public String getObjectIdName() {
        return "name";
    }

    @Override
    public Expression getIdWhereExpression(Lesson obj) {
        return 
                Expression.property(CLAZZ).equalTo(Expression.string(getObjectClass()))
                .and(Expression.property("name").equalTo(Expression.string(obj.getName())))
                .and(Expression.property("course").equalTo(Expression.string(obj.getCourse().getId())))
                ;
    }
}
