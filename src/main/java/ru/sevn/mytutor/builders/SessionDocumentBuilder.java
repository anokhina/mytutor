package ru.sevn.mytutor.builders;

import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.entities.Session;

@Component
public class SessionDocumentBuilder extends AbstractObjectDocumentBuilder<Session>{
    
    public SessionDocumentBuilder() {
        super(Session.class);
    }
    
    @Override
    public MutableDocument fillDocument(MutableDocument doc, Session user) {
        doc.setString("login", user.getLogin());
        doc.setString("sessionId", user.getSessionId());
        doc.setLong("rememberMe", user.getRememberMe());
        return doc;
    }

    @Override
    public String getObjectIdName() {
        return "sessionId";
    }

    @Override
    public Session getObjectInstance(MutableDocument doc) {
        System.out.println(">>>>>>>>>>>>>" + doc);
        return Session.builder()
                .login(doc.getString("login"))
                .sessionId(doc.getString("sessionId"))
                .rememberMe(doc.getLong("rememberMe"))
                .build()
                ;
    }

    @Override
    public Expression getIdWhereExpression(Session obj) {
        return getIdWhereExpression(obj.getSessionId());
    }

}
