package org.reldb.exemplars.java.backend.model.two;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "demo")
public class Demo {
    @Id
    @Column(name = "demo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long demoId;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column
    private String address;
}
