package com.imaugar.task_manager_api.dtos;

import com.imaugar.task_manager_api.enums.TaskStatus;

import lombok.Data;

@Data
public class TaskResponseDTO {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private String assignedUserName;
    private Long projectId;

    public TaskResponseDTO() {
    }

    public TaskResponseDTO(Long id, String name, String description, TaskStatus status, String assignedUserName, Long projectId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.assignedUserName = assignedUserName;
        this.projectId = projectId;
    }
}
