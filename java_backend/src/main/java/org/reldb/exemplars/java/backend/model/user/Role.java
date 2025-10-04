package org.reldb.exemplars.java.backend.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue
    @SequenceGenerator(name = "roles_seq", allocationSize = 1)
    private long roleId;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String description;

    @Setter
    @Column
    private boolean active;
}
