package com.forgeflow.service;

import com.forgeflow.dto.CreateProjectRequest;
import com.forgeflow.dto.ProjectResponse;
import com.forgeflow.dto.UpdateProjectRequest;
import com.forgeflow.entity.Project;
import com.forgeflow.entity.ProjectMember;
import com.forgeflow.entity.User;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.ProjectMemberRepository;
import com.forgeflow.repository.ProjectRepository;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final ProjectMemberRepository projectMemberRepository;


    // =========================================================
    // CREATE PROJECT
    // =========================================================

    public ProjectResponse createProject(
            CreateProjectRequest request,
            String userEmail
    ) {

        User owner =
                getUserByEmail(userEmail);

        Project project =
                new Project();

        project.setName(
                request.getName()
        );

        project.setDescription(
                request.getDescription()
        );

        project.setOwner(owner);

        Project savedProject =
                projectRepository.save(project);

        return new ProjectResponse(
                savedProject
        );
    }


    // =========================================================
    // GET ONE PROJECT
    // owner OR project member can access
    // =========================================================

    public ProjectResponse getProject(
            Long id,
            String userEmail
    ) {

        Project project =
                getProjectEntity(id);

        User currentUser =
                getUserByEmail(userEmail);

        checkProjectAccess(
                project,
                currentUser
        );

        return new ProjectResponse(project);
    }


    // =========================================================
    // GET ALL ACCESSIBLE PROJECTS
    //
    // returns:
    // 1. projects owned by user
    // 2. projects where user is a member
    // =========================================================

    public List<ProjectResponse> getAccessibleProjects(
            String userEmail
    ) {

        User user =
                getUserByEmail(userEmail);

        List<Project> ownedProjects =
                projectRepository
                        .findByOwnerId(
                                user.getId()
                        );

        List<ProjectMember> memberships =
                projectMemberRepository
                        .findByUserId(
                                user.getId()
                        );

        /*
         * LinkedHashMap prevents duplicates.
         *
         * Key   = project ID
         * Value = Project
         */
        Map<Long, Project> accessibleProjects =
                new LinkedHashMap<>();


        // Add projects owned by user
        for (Project project : ownedProjects) {

            accessibleProjects.put(
                    project.getId(),
                    project
            );
        }


        // Add projects where user is a member
        for (ProjectMember membership : memberships) {

            Project project =
                    membership.getProject();

            accessibleProjects.put(
                    project.getId(),
                    project
            );
        }


        return accessibleProjects
                .values()
                .stream()
                .map(ProjectResponse::new)
                .toList();
    }


    // =========================================================
    // GET PROJECTS OWNED BY CURRENT USER
    // =========================================================

    public List<ProjectResponse> getMyProjects(
            String userEmail
    ) {

        User user =
                getUserByEmail(userEmail);

        return projectRepository
                .findByOwnerId(
                        user.getId()
                )
                .stream()
                .map(ProjectResponse::new)
                .toList();
    }


    // =========================================================
    // UPDATE PROJECT
    // owner only
    // =========================================================

    public ProjectResponse updateProject(
            Long id,
            UpdateProjectRequest request,
            String userEmail
    ) {

        Project project =
                getProjectEntity(id);

        User currentUser =
                getUserByEmail(userEmail);

        checkOwnership(
                project,
                currentUser
        );


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

        return new ProjectResponse(
                updatedProject
        );
    }


    // =========================================================
    // DELETE PROJECT
    // owner only
    // =========================================================

    public void deleteProject(
            Long id,
            String userEmail
    ) {

        Project project =
                getProjectEntity(id);

        User currentUser =
                getUserByEmail(userEmail);

        checkOwnership(
                project,
                currentUser
        );

        projectRepository.delete(project);
    }


    // =========================================================
    // GET PROJECT ENTITY
    // =========================================================

    private Project getProjectEntity(
            Long id
    ) {

        return projectRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found with id: "
                                        + id
                        )
                );
    }


    // =========================================================
    // GET USER
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
    // PROJECT ACCESS
    //
    // owner OR member
    // =========================================================

    private void checkProjectAccess(
            Project project,
            User currentUser
    ) {

        boolean isOwner =
                project
                        .getOwner()
                        .getId()
                        .equals(
                                currentUser.getId()
                        );

        boolean isMember =
                projectMemberRepository
                        .existsByProjectIdAndUserId(
                                project.getId(),
                                currentUser.getId()
                        );


        if (!isOwner && !isMember) {

            throw new AccessDeniedException(
                    "You do not have access to this project"
            );
        }
    }


    // =========================================================
    // OWNER ONLY
    // =========================================================

    private void checkOwnership(
            Project project,
            User currentUser
    ) {

        if (!project
                .getOwner()
                .getId()
                .equals(
                        currentUser.getId()
                )) {

            throw new AccessDeniedException(
                    "Only the project owner can perform this action"
            );
        }
    }
}