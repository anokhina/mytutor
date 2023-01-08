package ru.sevn.mytutor.views.course;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.Binder;
import java.util.stream.Stream;
import ru.sevn.mytutor.entities.Lesson;
import ru.sevn.util.DateUtil;

public class LessonPanel extends VerticalLayout {
    
    private Binder<Lesson> binder = new Binder(Lesson.class);
    
    private TextField name = new TextField("Name");
    private TextField fullName = new TextField("Full name");
    private TextArea homeAssignment = new TextArea("Home assignment");
    private DatePicker date = new DatePicker("Date");

    public LessonPanel() {
        setWidthFull();
        add(name, fullName, homeAssignment, date);
        Stream<HasSize> s = Stream.of(name, fullName, homeAssignment, date);
        name.setEnabled(false);
        
        s.forEach(el -> el.setWidthFull());
        
        binder.forField(date)
                .withConverter(ld -> DateUtil.ms(ld), ms -> DateUtil.getLocalDate(ms))
                .bind("date");
        
        date.addValueChangeListener(evt -> {
            name.setValue("Урок " + DateUtil.day(evt.getValue()));
        });
        binder.bindInstanceFields(this);
    }

    public Binder<Lesson> getBinder() {
        return binder;
    }
    
}
