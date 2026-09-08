package com.forgeflow.service;

import com.forgeflow.dto.ProjectStatsResponse;
import com.forgeflow.entity.Project;
import com.forgeflow.entity.TaskPriority;
import com.forgeflow.entity.TaskStatus;
import com.forgeflow.entity.User;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.ProjectMemberRepository;
import com.forgeflow.repository.ProjectRepository;
import com.forgeflow.repository.TaskRepository;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final ProjectMemberRepository projectMemberRepository;


    public ProjectStatsResponse getProjectStats(
            Long projectId,
            String userEmail
    ) {

        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Project not found with id: "
                                                + projectId
                                )
                        );


        User user =
                userRepository
                        .findByEmail(userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );


        boolean owner =
                project
                        .getOwner()
                        .getId()
                        .equals(user.getId());


        boolean member =
                projectMemberRepository
                        .existsByProjectIdAndUserId(
                                projectId,
                                user.getId()
                        );


        if (!owner && !member) {

            throw new AccessDeniedException(
                    "You do not have access to this project"
            );
        }


        long total =
                taskRepository
                        .countByProjectId(
                                projectId
                        );


        long todo =
                taskRepository
                        .countByProjectIdAndStatus(
                                projectId,
                                TaskStatus.TODO
                        );


        long inProgress =
                taskRepository
                        .countByProjectIdAndStatus(
                                projectId,
                                TaskStatus.IN_PROGRESS
                        );


        long completed =
                taskRepository
                        .countByProjectIdAndStatus(
                                projectId,
                                TaskStatus.COMPLETED
                        );


        long highPriority =
                taskRepository
                        .countByProjectIdAndPriority(
                                projectId,
                                TaskPriority.HIGH
                        );


        long assigned =
                taskRepository
                        .countByProjectIdAndAssignedUserIsNotNull(
                                projectId
                        );


        long unassigned =
                taskRepository
                        .countByProjectIdAndAssignedUserIsNull(
                                projectId
                        );


        return new ProjectStatsResponse(
                total,
                todo,
                inProgress,
                completed,
                highPriority,
                assigned,
                unassigned
        );
    }
}