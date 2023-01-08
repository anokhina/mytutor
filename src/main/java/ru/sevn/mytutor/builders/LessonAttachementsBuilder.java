package ru.sevn.mytutor.builders;

import com.couchbase.lite.Blob;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sevn.mytutor.dao.LessonDaoComponent;
import ru.sevn.mytutor.entities.LessonAttachements;

@Component
public class LessonAttachementsBuilder extends AbstractObjectDocumentBuilder<LessonAttachements>{
    
    @Autowired
    private LessonDaoComponent lessonDaoComponent;
    
    public LessonAttachementsBuilder() {
        super(LessonAttachements.class);
    }

    @Override
    public MutableDocument fillDocument(MutableDocument doc, LessonAttachements ent) {
        Blob blob = new Blob(ent.getMimeType(), ent.getData()); 
        doc.setBlob("data", blob);
        
        doc.setString("description", ent.getDescription());
        doc.setString("mimeType", ent.getMimeType());
        
        doc.setString("lesson", ent.getLesson().getId());
        return doc;
    }

    @Override
    public LessonAttachements getObjectInstance(boolean full, MutableDocument doc) {
        var res = super.getObjectInstance(full, doc);
        if (full) {
            Blob blob = doc.getBlob("data");
            res.setData(blob.getContent());
        }
        return res;
    }

    @Override
    public LessonAttachements getObjectInstance(MutableDocument doc) {
        
        return LessonAttachements.builder()
                .description(doc.getString("description"))
                .mimeType(doc.getString("mimeType"))
                .lesson(lessonDaoComponent.findById(doc.getString("lesson")))
                .build();
    }
}
