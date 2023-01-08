package ru.sevn.mytutor.builders;

import ru.sevn.mytutor.dao.UserDaoComponent;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDocument;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.entities.Course;

@Component
public class CourseDocumentBuilder extends AbstractObjectDocumentBuilder<Course>{
    
    @Autowired
    private UserDaoComponent userDaoComponent;
    
    public CourseDocumentBuilder() {
        super(Course.class);
    }
    
    @Override
    public MutableDocument fillDocument(MutableDocument doc, Course user) {
        doc.setString("name", user.getName());
        doc.setString("tutor", user.getTutor().getId());
        var students = new MutableArray(
                user.getStudents().stream().map(e -> e.getId()).collect(Collectors.toList())
        );
        
        doc.setArray("students", students);
        return doc;
    }

    @Override
    public String getObjectIdName() {
        return "name";
    }

    @Override
    public Course getObjectInstance(MutableDocument doc) {
        var c = Course.builder()
                .name(doc.getString("name"))
                .tutor(userDaoComponent.findById(doc.getString("tutor")))
                .build()
                ;
        doc.getArray("students").forEach(el -> {
            var id =  el.toString();
            var student = userDaoComponent.findById(id);
            c.getStudents().add(student);
        });
        
        return c;
    }

    @Override
    public Expression getIdWhereExpression(Course obj) {
        return getIdWhereExpression(obj.getName());
    }

}
