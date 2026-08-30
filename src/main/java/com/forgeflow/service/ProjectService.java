package com.forgeflow.service;

import com.forgeflow.dto.CreateProjectRequest;
import com.forgeflow.dto.ProjectResponse;
import com.forgeflow.dto.UpdateProjectRequest;
import com.forgeflow.entity.Project;
import com.forgeflow.entity.User;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.ProjectRepository;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;


    // =========================================================
    // CREATE PROJECT
    // =========================================================

    public ProjectResponse createProject(
            CreateProjectRequest request,
            String userEmail
    ) {

        User owner = getUserByEmail(userEmail);

        Project project = new Project();

        project.setName(request.getName());

        project.setDescription(request.getDescription());

        project.setOwner(owner);

        Project savedProject =
                projectRepository.save(project);

        return new ProjectResponse(savedProject);
    }


    // =========================================================
    // GET PROJECT
    // =========================================================

    public ProjectResponse getProject(Long id) {

        Project project =
                projectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Project not found with id: " + id
                                )
                        );

        return new ProjectResponse(project);
    }


    // =========================================================
    // GET ALL PROJECTS
    // =========================================================

    public List<ProjectResponse> getAllProjects() {

        return projectRepository
                .findAll()
                .stream()
                .map(ProjectResponse::new)
                .toList();
    }


    // =========================================================
    // GET MY PROJECTS
    // =========================================================

    public List<ProjectResponse> getMyProjects(
            String userEmail
    ) {

        User user = getUserByEmail(userEmail);

        return projectRepository
                .findByOwnerId(user.getId())
                .stream()
                .map(ProjectResponse::new)
                .toList();
    }


    // =========================================================
    // UPDATE PROJECT
    // =========================================================

    public ProjectResponse updateProject(
            Long id,
            UpdateProjectRequest request,
            String userEmail
    ) {

        Project project =
                projectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Project not found with id: " + id
                                )
                        );

        User currentUser = getUserByEmail(userEmail);

        checkOwnership(project, currentUser);

        if (request.getName() != null) {

            project.setName(
                    request.getName()
            );
        }

        if (request.getDescription() != null) {

            project.setDescription(
                    request.getDescription()
            );
        }

        if (request.getStatus() != null) {

            project.setStatus(
                    request.getStatus()
            );
        }

        Project updatedProject =
                projectRepository.save(project);

        return new ProjectResponse(updatedProject);
    }


    // =========================================================
    // DELETE PROJECT
    // =========================================================

    public void deleteProject(
            Long id,
            String userEmail
    ) {

        Project project =
                projectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Project not found with id: " + id
                                )
                        );

        User currentUser = getUserByEmail(userEmail);

        checkOwnership(project, currentUser);

        projectRepository.delete(project);
    }


    // =========================================================
    // GET USER BY EMAIL
    // =========================================================

    private User getUserByEmail(
            String email
    ) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }


    // =========================================================
    // OWNERSHIP CHECK
    // =========================================================

    private void checkOwnership(
            Project project,
            User currentUser
    ) {

        if (!project.getOwner()
                .getId()
                .equals(currentUser.getId())) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to modify this project"
            );
        }
    }
}