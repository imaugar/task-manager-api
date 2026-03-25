package com.imaugar.task_manager_api.facades;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import com.imaugar.task_manager_api.dtos.ProjectDTO;
import com.imaugar.task_manager_api.dtos.ProjectResponseDTO;
import com.imaugar.task_manager_api.services.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    //Crear proyecto (solo admin)
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ProjectResponseDTO createProject(@RequestBody ProjectDTO project) {
        return projectService.createProject(project);
    }

    //Obtener proyectos del usuario
    @GetMapping
    public ResponseEntity<?> getProjectsForUser() {
        return ResponseEntity.ok(projectService.getProjectsForUser());
    }

    //Añadir miembro a proyecto (solo admin)
    @PostMapping("/{projectId}/addmember")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ProjectResponseDTO> addMember(@PathVariable Long projectId, @RequestParam String username) {
        projectService.addMemberToProject(projectId, username);
        return ResponseEntity.ok().build();
    }

}