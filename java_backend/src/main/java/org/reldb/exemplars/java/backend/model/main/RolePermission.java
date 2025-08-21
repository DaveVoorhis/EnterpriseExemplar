package org.reldb.exemplars.java.backend.model.main;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "role_permissions")
@IdClass(RolePermissionPK.class)
public class RolePermission {
    @Id
    @Column(name = "role_id")
    private long roleId;

    @Id
    @Column(name = "permission_name")
    private String permissionName;
}
