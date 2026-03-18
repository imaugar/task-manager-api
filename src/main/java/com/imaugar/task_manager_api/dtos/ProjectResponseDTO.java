package com.imaugar.task_manager_api.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ProjectResponseDTO {
    private Long id;
    private String name;
    private String description;
    //Nombres de miembros del proyecto
    private List<String> memberNames;

    public ProjectResponseDTO(Long id, String name, String description, List<String> memberNames) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.memberNames = memberNames;
    }

}
