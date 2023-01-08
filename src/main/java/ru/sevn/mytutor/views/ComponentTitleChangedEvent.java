package ru.sevn.mytutor.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class ComponentTitleChangedEvent extends ComponentEvent<Component>{

    public ComponentTitleChangedEvent(Component source, boolean fromClient) {
        super(source, fromClient);
    }

}
