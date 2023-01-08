package ru.sevn.mytutor.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
public class UserLessonHomeWork extends BaseObject {
    @NonNull
    private User user;
    @NonNull
    private Lesson lesson;
    @NonNull
    private String homeWork;
}
