package ru.sevn.mytutor.entities;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
public class User extends BaseObject {
    @NonNull
    @EqualsAndHashCode.Include
    private String login;
    @NonNull
    private String name;
    @NonNull
    private UserType userType;
    
}
