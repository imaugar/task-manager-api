package com.imaugar.task_manager_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import com.imaugar.task_manager_api.enums.TaskStatus;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    //Proyecto al que pertenece la tarea
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    //Usuario asignado a la tarea (si la tarea no está asignada, es null)
    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

}
