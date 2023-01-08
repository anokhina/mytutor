package ru.sevn.va.renderer.lit;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LitRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class Tag<OBJ> {

    private final String name;
    private List<TagAttribute<OBJ>> attributes = new ArrayList();
    private Function<OBJ, String> textProducer = o -> "";

    public Tag(String n) {
        this.name = n;
    }

    public Tag<OBJ> addTagContent(Function<OBJ, String> textProducer) {
        this.textProducer = textProducer;
        return this;
    }

    public Tag<OBJ> addAttribute(TagAttribute<OBJ> ta) {
        attributes.add(ta);
        return this;
    }

    public LitRenderer<OBJ> getLitRenderer() {
        java.lang.StringBuilder sb = new StringBuilder();
        sb.append("<").append(name).append(" ");
        sb.append(attributes.stream().map(e -> e.getTemplate()).collect(Collectors.joining(" ")));
        sb.append(">${item.text}</").append(name).append(">");
        com.vaadin.flow.data.renderer.LitRenderer<OBJ> res = LitRenderer.<OBJ>of(sb.toString());
        res.withProperty("text", o -> textProducer.apply(o));
        return res;
    }

    public static <T> Tag<T> of(Class<T> cls, String name) {
        return new Tag<T>(name);
    }
    public static <T> Tag<T> of(Grid<T> g, String name) {
        return new Tag<T>(name);
    }
}
