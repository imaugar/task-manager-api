package com.imaugar.task_manager_api.repositories;

import com.imaugar.task_manager_api.entities.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    //Encontrar tareas por ID de proyecto
    List<Task> findByProjectId(Long projectId);

    //Encontrar tareas por id de usuario asignado
    List<Task> findByAssignedUserId(Long userId);

}
