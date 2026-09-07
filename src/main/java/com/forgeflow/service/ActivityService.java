package com.forgeflow.service;

import com.forgeflow.dto.ActivityResponse;
import com.forgeflow.entity.*;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.ActivityRepository;
import com.forgeflow.repository.ProjectMemberRepository;
import com.forgeflow.repository.ProjectRepository;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final ProjectMemberRepository projectMemberRepository;


    // =========================================================
    // RECORD ACTIVITY
    // =========================================================

    public void record(
            ActivityType type,
            String message,
            Project project,
            User user,
            Long taskId
    ) {

        Activity activity =
                new Activity();

        activity.setType(type);

        activity.setMessage(message);

        activity.setProject(project);

        activity.setUser(user);

        activity.setTaskId(taskId);

        activityRepository.save(activity);
    }


    // =========================================================
    // GET PROJECT ACTIVITY
    // =========================================================

    public List<ActivityResponse> getProjectActivity(
            Long projectId,
            String userEmail,
            int limit
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


        limit =
                Math.min(
                        Math.max(limit, 1),
                        100
                );


        return activityRepository
                .findByProjectIdOrderByCreatedAtDesc(
                        projectId,
                        PageRequest.of(
                                0,
                                limit
                        )
                )
                .stream()
                .map(ActivityResponse::new)
                .toList();
    }
}