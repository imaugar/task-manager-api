package com.imaugar.task_manager_api.services;

import com.imaugar.task_manager_api.dtos.ProjectResponseDTO;


import com.imaugar.task_manager_api.dtos.ProjectDTO;
import com.imaugar.task_manager_api.repositories.ProjectRepository;
import com.imaugar.task_manager_api.repositories.UserRepository;
import com.imaugar.task_manager_api.entities.Project;
import com.imaugar.task_manager_api.entities.User;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }
    
    // Crear un proyecto (solo admin)
    public ProjectResponseDTO createProject(ProjectDTO project){
        //Conseguir datos del admin
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setDescription(project.getDescription());
        newProject.getMembers().add(user);

        // Guardar el proyecto en la base de datos
        projectRepository.save(newProject);
        return toDTO(newProject);
    }

    //Encontrar proyecto donde está el usuario
    public List<ProjectResponseDTO> getProjectsForUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        return projectRepository.findByMembersId(user.getId())
        .stream()
        .map(project -> toDTO(project))
        .toList();
    }

    //Añadir usuario a proyecto (solo admin)
    public ProjectResponseDTO addMemberToProject(Long projectId, String username){
        Project project = projectRepository.findById(projectId).orElseThrow();
        User user = userRepository.findByUsername(username).orElseThrow();

        project.getMembers().add(user);
        projectRepository.save(project);
        return toDTO(project);
    }

    //Convertir a DTO
    public ProjectResponseDTO toDTO(Project project){
        return new ProjectResponseDTO(project.getId(),project.getName(),project.getDescription(),project.getMembers().stream().map(User::getUsername).toList());
    }

}
