package org.reldb.exemplars.java.backend.api.mappers;

import org.reldb.exemplars.java.backend.api.model.RoleOut;
import org.reldb.exemplars.java.backend.model.user.Role;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    List<RoleOut> toRoleOut(List<Role> roles);
    RoleOut toRoleOut(Role role);
}
