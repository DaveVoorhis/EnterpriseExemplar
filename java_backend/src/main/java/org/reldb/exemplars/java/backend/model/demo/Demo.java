package org.reldb.exemplars.java.backend.model.demo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "demo")
public class Demo {
    @Id
    @Column(name = "demo_id")
    @GeneratedValue
    @SequenceGenerator(name = "demo_id", allocationSize = 1)
    private long demoId;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column
    private String address;
}
