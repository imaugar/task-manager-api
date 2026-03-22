package com.imaugar.task_manager_api.facades;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.imaugar.task_manager_api.services.TaskService;
import com.imaugar.task_manager_api.dtos.TaskDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.imaugar.task_manager_api.dtos.TaskResponseDTO;
import com.imaugar.task_manager_api.enums.TaskStatus;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //Crear tarea (solo admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskDTO task) {
        TaskResponseDTO createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    //Obtener las tareas de un proyecto
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTasksForProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksForProject(projectId));
    }

    //Obtener tareas asignadas al usuario logeado
    @GetMapping("/mytasks")
    public ResponseEntity<?> getMyTasks() {
        return ResponseEntity.ok(taskService.getTasksForUser());
    }

    //Actualizar estado de una tarea (solo usuario asignado)
    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(@PathVariable Long taskId, @RequestBody TaskStatus updatedStatus) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, updatedStatus));
    }

    //Asignar tarea a usuario (solo admin)
    @PutMapping("/{taskId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponseDTO> assignTaskToUser(@PathVariable Long taskId, @RequestBody Long userId) {
        return ResponseEntity.ok(taskService.assignTaskToMember(taskId, userId));
    }

}
