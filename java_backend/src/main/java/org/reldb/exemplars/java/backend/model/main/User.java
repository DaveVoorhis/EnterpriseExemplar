package org.reldb.exemplars.java.backend.model.main;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "app_users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue
    @SequenceGenerator(name = "app_users_id", allocationSize = 1)
    private long userId;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column
    private boolean enabled;

    @Setter
    @Column(name = "last_login", nullable = false)
    private Instant lastLogin;
}
