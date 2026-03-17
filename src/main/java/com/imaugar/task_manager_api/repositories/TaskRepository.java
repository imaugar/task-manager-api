package com.imaugar.task_manager_api.repositories;

import com.imaugar.task_manager_api.entities.Task;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    //Encontrar tarea por ID de proyecto
    Optional<Task> findByProjectId(Long projectId);

    //Encontrar tarea por id de usuario asignado
    Optional<Task> findByAssignedUserId(Long userId);

}
