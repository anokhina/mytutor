package ru.sevn.mytutor.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
public class Course extends BaseObject {
    @EqualsAndHashCode.Include
    @NonNull
    private String name;

    private User tutor;
    
    @Builder.Default
    private Set<User> students = new HashSet();
}
