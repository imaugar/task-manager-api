package com.imaugar.task_manager_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;


@Data
@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    //Usuarios que están en el proyecto
    @ManyToMany
    @JoinTable(
        name="project_users",
        joinColumns = @JoinColumn(name="project_id"),
        inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private List<User> members = new java.util.ArrayList<>();

    //Tareas asociadas al proyecto
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks = new java.util.ArrayList<>();

}
