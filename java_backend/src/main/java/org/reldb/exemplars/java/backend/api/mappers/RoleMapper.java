package org.reldb.exemplars.java.backend.api.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.reldb.exemplars.java.backend.api.model.RoleOut;
import org.reldb.exemplars.java.backend.model.user.Role;

@Mapper
public interface RoleMapper {
    List<RoleOut> toRoleOut(List<Role> roles);
    RoleOut toRoleOut(Role role);
}
