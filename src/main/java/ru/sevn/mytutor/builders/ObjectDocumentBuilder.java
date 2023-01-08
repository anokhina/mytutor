package ru.sevn.mytutor.builders;

import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.lang3.ObjectUtils;
import ru.sevn.mytutor.entities.BaseObject;

public interface ObjectDocumentBuilder<OBJ extends BaseObject> {
    public static final String CLAZZ = "clazz";
    default MutableDocument getDocument(OBJ ent) {
        MutableDocument doc;
        if (ObjectUtils.isEmpty(ent.getId())) {
            var id = UUID.randomUUID().toString().toLowerCase(Locale.ENGLISH);
            ent.setId(id);
            doc = new MutableDocument(id);
        } else {
            doc = new MutableDocument(ent.getId());
        }
        doc.setString(CLAZZ, getObjectClass());
        return fillDocument(doc, ent);
    }
    MutableDocument fillDocument(MutableDocument doc, OBJ ent);
    OBJ getObjectInstance(MutableDocument doc);
    
    default OBJ getObjectInstance(boolean full, MutableDocument doc) {
        return getObjectInstance(doc);
    }
    
    default OBJ getObject(boolean full, MutableDocument doc) {
        var obj = getObjectInstance(doc);
        obj.setId(doc.getId());
        return obj;
    }
    
    default String getObjectIdName() {
        return "id";
    }
    
    String getObjectClass();
    
    default Expression getIdWhereExpression(OBJ obj) {
        return getIdWhereExpression(obj.getId());
    }
    
    default Expression getIdWhereExpression(String id) {
        return getIdWhereExpression(Expression.string(id));
    }
    
    default Expression getIdWhereExpression(Expression exprObjectIdNameEqualTo) {
        return 
                getWhereExpression()
                .and(Expression.property(getObjectIdName()).equalTo(exprObjectIdNameEqualTo))
                ;
    }
    
    default Expression getWhereExpression() {
        return 
                Expression.property(CLAZZ).equalTo(Expression.string(getObjectClass()))
                ;
    }
}
