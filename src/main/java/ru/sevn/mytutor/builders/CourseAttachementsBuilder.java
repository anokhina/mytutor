package ru.sevn.mytutor.builders;

import com.couchbase.lite.Blob;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.dao.CourseDaoComponent;
import ru.sevn.mytutor.entities.CourseAttachements;

@Component
public class CourseAttachementsBuilder extends AbstractObjectDocumentBuilder<CourseAttachements>{
    
    @Autowired
    private CourseDaoComponent courseDaoComponent;
    
    public CourseAttachementsBuilder() {
        super(CourseAttachements.class);
    }

    @Override
    public MutableDocument fillDocument(MutableDocument doc, CourseAttachements ent) {
        Blob blob = new Blob(ent.getMimeType(), ent.getData()); 
        doc.setBlob("data", blob);
        
        doc.setString("description", ent.getDescription());
        doc.setString("mimeType", ent.getMimeType());
        
        doc.setString("course", ent.getCourse().getId());
        return doc;
    }

    @Override
    public CourseAttachements getObject(boolean full, MutableDocument doc) {
        var res = super.getObjectInstance(full, doc);
        if (full) {
            Blob blob = doc.getBlob("data");
            res.setData(blob.getContent());
        }
        return res;
    }
    
    @Override
    public CourseAttachements getObjectInstance(MutableDocument doc) {
        
        return CourseAttachements.builder()
                .description(doc.getString("description"))
                .mimeType(doc.getString("mimeType"))
                .course(courseDaoComponent.findById(doc.getString("course")))
                .build();
    }
}
