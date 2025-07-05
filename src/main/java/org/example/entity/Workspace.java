package org.example.entity;

import jakarta.persistence.*;
import org.example.enums.WorkspaceType;

import java.math.BigDecimal;

@Entity
public class Workspace implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private WorkspaceType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    protected Workspace() {
    }

    public Workspace(String name, WorkspaceType type, BigDecimal price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkspaceType getType() {
        return type;
    }

    public void setType(WorkspaceType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
