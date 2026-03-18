package com.imaugar.task_manager_api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectDTO {
    @NotBlank
    private String name;
    private String description;
}
