package com.imaugar.task_manager_api.dtos;

import com.imaugar.task_manager_api.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskStatusDTO {
    @NotNull
    private TaskStatus status;
}
