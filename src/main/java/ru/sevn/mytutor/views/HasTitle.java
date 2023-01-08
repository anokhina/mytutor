package ru.sevn.mytutor.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.shared.Registration;

public interface HasTitle {
    String getPageTitle();
    
    default void fireComponentTitleChangedEvent() {
        if (this instanceof Component) {
            var cmp = (Component) this;
            ComponentUtil.fireEvent(cmp, new ComponentTitleChangedEvent(cmp, false));
        } else {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "fireComponentTitleChangedEvent"));
        }        
    }
    
    default Registration addComponentTitleChangedListener(ComponentEventListener<ComponentTitleChangedEvent> listener) {
        if (this instanceof Component) {
            return ComponentUtil.addListener((Component) this,
                    ComponentTitleChangedEvent.class, listener);
        } else {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "addComponentTitleChangedListener"));
        }        
    }
}
