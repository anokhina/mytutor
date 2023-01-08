package ru.sevn.mytutor.builders;

import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.entities.User;
import ru.sevn.mytutor.entities.UserType;

@Component
public class UserDocumentBuilder extends AbstractObjectDocumentBuilder<User>{
    
    public UserDocumentBuilder() {
        super(User.class);
    }
    
    @Override
    public MutableDocument fillDocument(MutableDocument doc, User user) {
        doc.setString("login", user.getLogin());
        doc.setString("name", user.getName());
        doc.setString("userType", user.getUserType().name());
        return doc;
    }

    @Override
    public String getObjectIdName() {
        return "login";
    }

    @Override
    public User getObjectInstance(MutableDocument doc) {
        return User.builder()
                .login(doc.getString("login"))
                .name(doc.getString("name"))
                .userType(UserType.valueOf(doc.getString("userType")))
                .build()
                ;
    }

    @Override
    public Expression getIdWhereExpression(User obj) {
        return getIdWhereExpression(obj.getLogin());
    }

}
