package org.reldb.exemplars.java.backend.model.main;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_roles")
@IdClass(UserRolePK.class)
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "role_id")
    private long roleId;
}
