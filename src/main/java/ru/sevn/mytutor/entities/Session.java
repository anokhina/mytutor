package ru.sevn.mytutor.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import ru.sevn.mytutor.security.HasRememberMe;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
public class Session extends BaseObject implements HasRememberMe {
    @NonNull
    @EqualsAndHashCode.Include
    private String sessionId;
    
    @NonNull
    private String login;
    private Long rememberMe;

}
