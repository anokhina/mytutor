package ru.sevn.mytutor.dao;

import com.couchbase.lite.ArrayFunction;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Expression;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.entities.*;
import ru.sevn.mytutor.builders.*;
import ru.sevn.mytutor.security.AppUser;

@Component
public class CourseDaoComponent extends DaoComponent<Course, CourseDocumentBuilder> {
    
    @Autowired
    private CourseDocumentBuilder documentBuilder;
    
    public Course addUserOnNotFound(Course obj) throws CouchbaseLiteException {
        return addOnNotFound(obj);
    }

    @Override
    public CourseDocumentBuilder getObjectDocumentBuilder() {
        return documentBuilder;
    }

    @Override
    public String getObjectId(Course obj) {
        return obj.getName();
    }
    
    public List<Course> findOwnObjects(User user) throws CouchbaseLiteException {
        return findObjects(getObjectDocumentBuilder().getWhereExpression().and(
                Expression.property("tutor").equalTo(Expression.string(user.getId()))
        ));
    }
    public List<Course> findInObjects(User user) throws CouchbaseLiteException {
        /*
        .where(ArrayFunction.contains(Expression.property("languages"), value: Expression.string("en"))) .or(Expression.propertyArrayFunction.contains(Expression.property("languages"), value: Expression.string("it")))
        */
        return findObjects(getObjectDocumentBuilder().getWhereExpression().and(
                ArrayFunction.contains(Expression.property("students"), Expression.string(user.getId()))
        )
        );
    }
}
