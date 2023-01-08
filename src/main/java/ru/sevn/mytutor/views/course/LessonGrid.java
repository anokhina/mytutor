package ru.sevn.mytutor.views.course;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Function;
import ru.sevn.mytutor.entities.Lesson;
import ru.sevn.va.dialog.VaMessageDialog;
import ru.sevn.va.renderer.lit.Tag;

public class LessonGrid extends Grid<Lesson>{
    private LessonPanel editorPanel = new LessonPanel();
    private VaMessageDialog<LessonPanel> editorDialog;
    
    public LessonGrid(Function<Lesson, OperationResult> saveFn, BiConsumer<Lesson, OperationResult> onSaved) {
        super(Lesson.class, false);
        var grid = this;
        editorDialog =  VaMessageDialog.getMessageDialogYN("Edit", editorPanel, () -> {
            if (editorPanel.getBinder().validate().isOk()) {
                var bean = editorPanel.getBinder().getBean();
                var fnRes = saveFn.apply(bean);
                if (fnRes.isError()) {
                    Notification.show("ОШИБКА: " + fnRes.getMessage());
                    return false;
                } else {
                    onSaved.accept(bean, fnRes);
                    return true;
                }
            } else {
                return false;
            }
        });
        
        addComponentColumn(bean -> {
            Button editButton = new Button (VaadinIcon.EDIT.create ());
            editButton.addClassName ("blank");
            editButton.addClickListener (e -> {
                editorPanel.getBinder().setBean(bean);
                editorDialog.open();
            });
            return editButton;
        })
                .setWidth ("80px")
                .setFlexGrow (0)
                .setFrozen(true)
                .setResizable (true);
        
        addColumn(
                Tag.of(grid, "div")
                        .addTagContent(o -> new SimpleDateFormat("dd.MM.yyyy").format(new Date(o.getDate())))
                .getLitRenderer()
        ).setHeader("Date")
                ;
        addColumn(
                Tag.of(grid, "div")
                        .addTagContent(o -> o.getName())
                .getLitRenderer()
        ).setHeader("Name")
                ;
        addColumn(
                Tag.of(grid, "div")
                        .addTagContent(o -> o.getFullName())
                .getLitRenderer()
        ).setHeader("FullName")
                ;
        addColumn(
                Tag.of(grid, "div")
                        .addTagContent(o -> o.getHomeAssignment())
                .getLitRenderer()
        ).setHeader("Home Assignment")
                ;
    }

    public LessonPanel getEditorPanel() {
        return editorPanel;
    }

    public void setEditorPanel(LessonPanel editorPanel) {
        this.editorPanel = editorPanel;
    }

    public VaMessageDialog<LessonPanel> getEditorDialog() {
        return editorDialog;
    }

    public void setEditorDialog(VaMessageDialog<LessonPanel> editorDialog) {
        this.editorDialog = editorDialog;
    }

}
