package ru.sevn.mytutor.builders;

import com.couchbase.lite.MutableDocument;
import ru.sevn.mytutor.entities.BaseObject;
import static ru.sevn.mytutor.builders.ObjectDocumentBuilder.CLAZZ;

public abstract class AbstractObjectDocumentBuilder<OBJ extends BaseObject> implements ObjectDocumentBuilder<OBJ>{
    private final Class<OBJ> clazz;
    
    protected AbstractObjectDocumentBuilder(Class<OBJ> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public final String getObjectClass() {
        return clazz.getName();
    }
}
