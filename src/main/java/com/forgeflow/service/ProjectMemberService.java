package com.forgeflow.service;

import com.forgeflow.dto.ProjectMemberResponse;
import com.forgeflow.entity.Project;
import com.forgeflow.entity.ProjectMember;
import com.forgeflow.entity.User;
import com.forgeflow.exception.DuplicateResourceException;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.ProjectMemberRepository;
import com.forgeflow.repository.ProjectRepository;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;


    // ADD MEMBER
    public ProjectMemberResponse addMember(
            Long projectId,
            Long userId,
            String ownerEmail
    ) {

        Project project =
                getProject(projectId);

        User owner =
                getUserByEmail(ownerEmail);

        checkProjectOwner(
                project,
                owner
        );

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: " + userId
                                )
                        );

        if (projectMemberRepository
                .existsByProjectIdAndUserId(
                        projectId,
                        userId
                )) {

            throw new DuplicateResourceException(
                    "User is already a member of this project"
            );
        }

        ProjectMember member =
                new ProjectMember();

        member.setProject(project);

        member.setUser(user);

        ProjectMember savedMember =
                projectMemberRepository.save(member);

        return new ProjectMemberResponse(
                savedMember
        );
    }


    // GET MEMBERS
    public List<ProjectMemberResponse> getMembers(
            Long projectId
    ) {

        if (!projectRepository.existsById(projectId)) {

            throw new ResourceNotFoundException(
                    "Project not found with id: " + projectId
            );
        }

        return projectMemberRepository
                .findByProjectId(projectId)
                .stream()
                .map(ProjectMemberResponse::new)
                .toList();
    }


    // REMOVE MEMBER
    public void removeMember(
            Long projectId,
            Long userId,
            String ownerEmail
    ) {

        Project project =
                getProject(projectId);

        User owner =
                getUserByEmail(ownerEmail);

        checkProjectOwner(
                project,
                owner
        );

        if (!projectMemberRepository
                .existsByProjectIdAndUserId(
                        projectId,
                        userId
                )) {

            throw new ResourceNotFoundException(
                    "User is not a member of this project"
            );
        }

        projectMemberRepository
                .deleteByProjectIdAndUserId(
                        projectId,
                        userId
                );
    }


    private Project getProject(
            Long projectId
    ) {

        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found with id: "
                                        + projectId
                        )
                );
    }


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


    private void checkProjectOwner(
            Project project,
            User currentUser
    ) {

        if (!project.getOwner()
                .getId()
                .equals(currentUser.getId())) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "Only the project owner can manage members"
            );
        }
    }
}