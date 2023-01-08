package ru.sevn.va.renderer.lit;

import com.vaadin.flow.data.renderer.LitRenderer;
import java.util.function.BiConsumer;

public class OnClick<OBJ> extends TagAttribute<OBJ> {

    private final BiConsumer<OBJ, ClickEvent> onClick;

    public OnClick(BiConsumer<OBJ, ClickEvent> onClick) {
        super("onClick", "@click=${(e) => {onClick(e.shiftKey, e.ctrlKey, e.altKey, e.metaKey); e.preventDefault();}}");
        this.onClick = onClick;
    }

    @Override
    public LitRenderer<OBJ> addValueFuntionality(LitRenderer<OBJ> lr) {
        lr.withFunction(getProperty(), (el, args) -> {
            ru.sevn.va.renderer.lit.ClickEvent evt = new ClickEvent();
            evt.shiftKey = args.getBoolean(0);
            evt.ctrlKey = args.getBoolean(1);
            evt.altKey = args.getBoolean(2);
            evt.metaKey = args.getBoolean(3);
            onClick.accept(el, evt);
        });
        return lr;
    }

}
