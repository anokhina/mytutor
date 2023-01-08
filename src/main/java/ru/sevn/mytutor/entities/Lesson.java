package ru.sevn.mytutor.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
public class Lesson extends BaseObject {
    @NonNull
    @EqualsAndHashCode.Include
    private String name;
    @NonNull
    private Course course;
    @NonNull
    private Long date;
    private String fullName;
    private String homeAssignment;
}
