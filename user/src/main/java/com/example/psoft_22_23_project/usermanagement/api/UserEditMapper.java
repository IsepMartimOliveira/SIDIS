package com.example.psoft_22_23_project.usermanagement.api;

import com.example.psoft_22_23_project.usermanagement.model.Role;
import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.services.CreateUserRequest;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = "spring")
public abstract class UserEditMapper {

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringToRole")
    public abstract User create(CreateUserRequest request);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringToRole")
    public abstract void update(UpdateUserRequest request, @MappingTarget User user);

    @Named("stringToRole")
    protected Set<Role> stringToRole(final Set<String> authorities) {
        if (authorities != null) {
            return authorities.stream().map(Role::new).collect(toSet());
        }
        return new HashSet<>();
    }

}