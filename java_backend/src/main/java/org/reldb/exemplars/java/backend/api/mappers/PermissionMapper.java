package org.reldb.exemplars.java.backend.api.mappers;

import org.reldb.exemplars.java.backend.api.model.PermissionOut;
import org.reldb.exemplars.java.backend.enums.Permissions;
import org.reldb.exemplars.java.backend.exception.custom.PermissionNotFoundException;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class PermissionMapper {
    public abstract List<PermissionOut> toPermissionOut(List<Permissions> permissions);

    public abstract List<Permissions> permissionNameToPermission(List<String> permissionNames);

    @Mapping(target = "name", expression = "java(permissions.name())")
    public abstract PermissionOut permissionsToPermissionOut(Permissions permissions);

    public Permissions permissionNameToPermission(String permissionName) {
        final Permissions permission;
        try {
            permission = Permissions.valueOf(permissionName);
        } catch (IllegalArgumentException iae) {
            throw new PermissionNotFoundException(permissionName);
        }
        return permission;
    }
}
