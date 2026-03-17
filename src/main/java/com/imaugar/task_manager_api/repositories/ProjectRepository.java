package com.imaugar.task_manager_api.repositories;

import com.imaugar.task_manager_api.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    //Encontrar proyecto por id de miembro
    Optional<Project> findByMemberId(Long memberId);
}
