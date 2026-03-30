package com.imaugar.task_manager_api.services;

import org.springframework.stereotype.Service;

import com.imaugar.task_manager_api.repositories.UserRepository;
import com.imaugar.task_manager_api.repositories.ProjectRepository;
import com.imaugar.task_manager_api.repositories.TaskRepository;
import com.imaugar.task_manager_api.dtos.TaskDTO;
import com.imaugar.task_manager_api.dtos.TaskResponseDTO;
import com.imaugar.task_manager_api.entities.Task;
import com.imaugar.task_manager_api.entities.User;
import com.imaugar.task_manager_api.enums.TaskStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }
    
    //Crear task (solo admin)
    public TaskResponseDTO createTask(TaskDTO task) {
        Task newTask = new Task();
        newTask.setName(task.getName());
        newTask.setDescription(task.getDescription());
        newTask.setStatus(TaskStatus.PENDING);
        newTask.setProject(projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("El proyecto especificado no existe")));

        if (task.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(task.getAssignedUserId()).orElseThrow();
            newTask.setAssignedUser(assignedUser);
        }

        Task savedTask = taskRepository.save(newTask);
        return toDTO(savedTask);
    }

    //Obtener tareas de proyecto
    public List<TaskResponseDTO> getTasksForProject(Long projectId) {
        return taskRepository.findByProjectId(projectId)
        .stream()
        .map(task -> toDTO(task))
        .toList();
    }

    //Obtener tareas asignadas a usuario
    public List<TaskResponseDTO> getTasksForUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        return taskRepository.findByAssignedUserId(user.getId())
        .stream()
        .map(task -> toDTO(task))
        .toList();
    }

    //Modificar el estado de una tarea (usuario asignado)
    public TaskResponseDTO updateTaskStatus(Long taskId, TaskStatus updatedStatus){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Task task = taskRepository.findById(taskId).orElseThrow();

        if(task.getAssignedUser() != null && task.getAssignedUser().getId().equals(user.getId())){
            task.setStatus(updatedStatus);
            Task updatedTask = taskRepository.save(task);
            return toDTO(updatedTask);
        } else {
            throw new AccessDeniedException("Solo el usuario asignado a la tarea puede modificar su estado");
        }
    }

    //Asignar tarea a usuario (solo admin y además miembro del proyecto)
    public TaskResponseDTO assignTaskToMember(Long taskId, Long userId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Task task = taskRepository.findById(taskId).orElseThrow();

        if (projectRepository.findById(task.getProject().getId()).orElseThrow().getMembers().stream().noneMatch(m -> m.getId().equals(user.getId()))) {
            throw new AccessDeniedException("Solo los administradores que pertenecen al proyecto pueden asignar tareas a usuarios");
        }
        User assignedUser = userRepository.findById(userId).orElseThrow();
        task.setAssignedUser(assignedUser);
        Task updatedTask = taskRepository.save(task);
        return toDTO(updatedTask);
    }

    //a DTO
    private TaskResponseDTO toDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setProjectId(task.getProject().getId());
        if (task.getAssignedUser() != null) {
            dto.setAssignedUserName(task.getAssignedUser().getUsername());
        }
        return dto;
    }
}
