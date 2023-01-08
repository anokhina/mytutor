package ru.sevn.mytutor.views.course;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import org.apache.commons.io.IOUtils;
import ru.sevn.mytutor.entities.BaseBlobObject;
import ru.sevn.va.upload.VaUpload;

public class AttachementPanel<BO extends BaseBlobObject> extends VerticalLayout {
    private BO blobObject;
    
    protected Upload upload = VaUpload.getOneFileUpload (new Upload (), 100*VaUpload.MB, dataLoad -> {
        var event = dataLoad.getEvent ();
        var fileName = event.getFileName ();
        blobObject.setDescription(fileName);
        blobObject.setMimeType(event.getMIMEType());

        try (var inputStream = dataLoad.getReceiver ().getInputStream ()) {
            var bytes = IOUtils.toByteArray (inputStream);
            blobObject.setData(bytes);
        }
        catch (Exception ex) {
            event.getUpload ().clearFileList ();
            ex.printStackTrace();
            Notification.show("Ошибка загрузки файла");
        }
    });

    public AttachementPanel() {
        setWidthFull();
        upload.setWidthFull();
        add(upload);
    }

    public BO getBlobObject() {
        return blobObject;
    }

    public void setBlobObject(BO blobObject) {
        this.blobObject = blobObject;
    }
    
}
