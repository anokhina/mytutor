package ru.sevn.mytutor.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
public class UserLessonHomeWorkAttachements extends BaseBlobObject {
    private UserLessonHomeWork userLessonHomeWork;
}
