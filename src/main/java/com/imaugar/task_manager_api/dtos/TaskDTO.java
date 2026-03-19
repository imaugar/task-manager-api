package com.imaugar.task_manager_api.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskDTO {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Long projectId;
    private Long assignedUserId;
}
