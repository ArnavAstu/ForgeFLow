package com.forgeflow.service;

import com.forgeflow.dto.AddMemberRequest;
import com.forgeflow.dto.ProjectMemberResponse;
import com.forgeflow.dto.UpdateMemberRoleRequest;
import com.forgeflow.entity.Project;
import com.forgeflow.entity.ProjectMember;
import com.forgeflow.entity.ProjectMemberRole;
import com.forgeflow.entity.User;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.ProjectMemberRepository;
import com.forgeflow.repository.ProjectRepository;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final ProjectMemberRepository projectMemberRepository;


    // =========================================================
    // ADD MEMBER
    // =========================================================

    public ProjectMemberResponse addMember(
            Long projectId,
            AddMemberRequest request,
            String ownerEmail
    ) {

        Project project =
                getProject(projectId);

        User owner =
                getUser(ownerEmail);

        checkOwner(project, owner);


        User user =
                userRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with email: "
                                                + request.getEmail()
                                )
                        );


        if (project.getOwner().getId()
                .equals(user.getId())) {

            throw new IllegalArgumentException(
                    "Project owner cannot be added as a member"
            );
        }


        if (projectMemberRepository
                .existsByProjectIdAndUserId(
                        projectId,
                        user.getId()
                )) {

            throw new IllegalArgumentException(
                    "User is already a project member"
            );
        }


        ProjectMember member =
                new ProjectMember();

        member.setProject(project);

        member.setUser(user);

        member.setRole(
                ProjectMemberRole.MEMBER
        );


        ProjectMember saved =
                projectMemberRepository.save(member);

        return new ProjectMemberResponse(saved);
    }


    // =========================================================
    // GET MEMBERS
    // =========================================================

    public List<ProjectMemberResponse> getMembers(
            Long projectId,
            String userEmail
    ) {

        Project project =
                getProject(projectId);

        User user =
                getUser(userEmail);

        checkProjectAccess(project, user);


        return projectMemberRepository
                .findByProjectId(projectId)
                .stream()
                .map(ProjectMemberResponse::new)
                .toList();
    }


    // =========================================================
    // REMOVE MEMBER
    // =========================================================

    public void removeMember(
            Long projectId,
            Long userId,
            String ownerEmail
    ) {

        Project project =
                getProject(projectId);

        User owner =
                getUser(ownerEmail);

        checkOwner(project, owner);


        ProjectMember member =
                projectMemberRepository
                        .findByProjectIdAndUserId(
                                projectId,
                                userId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Project member not found"
                                )
                        );


        projectMemberRepository.delete(member);
    }


    // =========================================================
    // UPDATE MEMBER ROLE
    // =========================================================

    public ProjectMemberResponse updateRole(
            Long projectId,
            Long userId,
            UpdateMemberRoleRequest request,
            String ownerEmail
    ) {

        Project project =
                getProject(projectId);

        User owner =
                getUser(ownerEmail);

        checkOwner(project, owner);


        ProjectMember member =
                projectMemberRepository
                        .findByProjectIdAndUserId(
                                projectId,
                                userId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Project member not found"
                                )
                        );


        member.setRole(
                request.getRole()
        );


        ProjectMember saved =
                projectMemberRepository.save(member);

        return new ProjectMemberResponse(saved);
    }


    // =========================================================
    // HELPERS
    // =========================================================

    private Project getProject(Long projectId) {

        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found"
                        )
                );
    }


    private User getUser(String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }


    private void checkOwner(
            Project project,
            User user
    ) {

        if (!project.getOwner().getId()
                .equals(user.getId())) {

            throw new AccessDeniedException(
                    "Only the project owner can perform this action"
            );
        }
    }


    private void checkProjectAccess(
            Project project,
            User user
    ) {

        boolean owner =
                project.getOwner().getId()
                        .equals(user.getId());


        boolean member =
                projectMemberRepository
                        .existsByProjectIdAndUserId(
                                project.getId(),
                                user.getId()
                        );


        if (!owner && !member) {

            throw new AccessDeniedException(
                    "You do not have access to this project"
            );
        }
    }
}