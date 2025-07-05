package org.example.entity;

import jakarta.persistence.*;

@Entity
public class Reservation implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Embedded
    private Interval interval;

    protected Reservation() {
    }

    public Reservation(String name, Workspace workspace, Interval interval) {
        this.name = name;
        this.workspace = workspace;
        this.interval = interval;
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

    public Workspace getWorkspace() {
        return workspace;
    }

    public Interval getInterval() {
        return interval;
    }
}
