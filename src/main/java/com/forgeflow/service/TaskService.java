package com.forgeflow.service;

import com.forgeflow.dto.CreateTaskRequest;
import com.forgeflow.dto.TaskResponse;
import com.forgeflow.dto.UpdateTaskRequest;
import com.forgeflow.entity.Project;
import com.forgeflow.entity.ProjectMember;
import com.forgeflow.entity.Task;
import com.forgeflow.entity.User;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.ProjectMemberRepository;
import com.forgeflow.repository.ProjectRepository;
import com.forgeflow.repository.TaskRepository;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final ProjectMemberRepository projectMemberRepository;


    // =========================================================
    // CREATE TASK
    // =========================================================

    public TaskResponse createTask(
            Long projectId,
            CreateTaskRequest request,
            String userEmail
    ) {

        Project project =
                getProject(projectId);

        User currentUser =
                getUserByEmail(userEmail);

        checkProjectAccess(
                project,
                currentUser
        );

        Task task =
                new Task();

        task.setTitle(
                request.getTitle()
        );

        task.setDescription(
                request.getDescription()
        );

        task.setPriority(
                request.getPriority()
        );

        task.setProject(project);

        Task savedTask =
                taskRepository.save(task);

        return new TaskResponse(savedTask);
    }


    // =========================================================
    // GET TASK
    // =========================================================

    public TaskResponse getTask(
            Long taskId,
            String userEmail
    ) {

        Task task =
                getTaskEntity(taskId);

        User currentUser =
                getUserByEmail(userEmail);

        checkProjectAccess(
                task.getProject(),
                currentUser
        );

        return new TaskResponse(task);
    }


    // =========================================================
    // GET PROJECT TASKS
    // =========================================================

    public List<TaskResponse> getProjectTasks(
            Long projectId,
            String userEmail
    ) {

        Project project =
                getProject(projectId);

        User currentUser =
                getUserByEmail(userEmail);

        checkProjectAccess(
                project,
                currentUser
        );

        return taskRepository
                .findByProjectId(projectId)
                .stream()
                .map(TaskResponse::new)
                .toList();
    }


    // =========================================================
    // UPDATE TASK
    // =========================================================

    public TaskResponse updateTask(
            Long taskId,
            UpdateTaskRequest request,
            String userEmail
    ) {

        Task task =
                getTaskEntity(taskId);

        User currentUser =
                getUserByEmail(userEmail);

        checkProjectAccess(
                task.getProject(),
                currentUser
        );

        if (request.getTitle() != null) {

            task.setTitle(
                    request.getTitle()
            );
        }

        if (request.getDescription() != null) {

            task.setDescription(
                    request.getDescription()
            );
        }

        if (request.getStatus() != null) {

            task.setStatus(
                    request.getStatus()
            );
        }

        if (request.getPriority() != null) {

            task.setPriority(
                    request.getPriority()
            );
        }

        Task updatedTask =
                taskRepository.save(task);

        return new TaskResponse(updatedTask);
    }


    // =========================================================
    // DELETE TASK
    // =========================================================

    public void deleteTask(
            Long taskId,
            String userEmail
    ) {

        Task task =
                getTaskEntity(taskId);

        User currentUser =
                getUserByEmail(userEmail);

        checkProjectOwner(
                task.getProject(),
                currentUser
        );

        taskRepository.delete(task);
    }


    // =========================================================
    // ASSIGN TASK
    // =========================================================

    public TaskResponse assignTask(
            Long taskId,
            Long userId,
            String ownerEmail
    ) {

        Task task =
                getTaskEntity(taskId);

        User owner =
                getUserByEmail(ownerEmail);

        checkProjectOwner(
                task.getProject(),
                owner
        );

        User assignedUser =
                userRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + userId
                                )
                        );

        boolean isMember =
                projectMemberRepository
                        .existsByProjectIdAndUserId(
                                task.getProject().getId(),
                                userId
                        );

        if (!isMember &&
                !task.getProject()
                        .getOwner()
                        .getId()
                        .equals(userId)) {

            throw new AccessDeniedException(
                    "Task can only be assigned to project members"
            );
        }

        task.setAssignedUser(
                assignedUser
        );

        Task savedTask =
                taskRepository.save(task);

        return new TaskResponse(savedTask);
    }


    // =========================================================
    // UNASSIGN TASK
    // =========================================================

    public TaskResponse unassignTask(
            Long taskId,
            String ownerEmail
    ) {

        Task task =
                getTaskEntity(taskId);

        User owner =
                getUserByEmail(ownerEmail);

        checkProjectOwner(
                task.getProject(),
                owner
        );

        task.setAssignedUser(null);

        Task savedTask =
                taskRepository.save(task);

        return new TaskResponse(savedTask);
    }


    // =========================================================
    // HELPER METHODS
    // =========================================================

    private Task getTaskEntity(
            Long taskId
    ) {

        return taskRepository
                .findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: "
                                        + taskId
                        )
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


    private void checkProjectAccess(
            Project project,
            User user
    ) {

        boolean owner =
                project.getOwner()
                        .getId()
                        .equals(user.getId());

        boolean member =
                projectMemberRepository
                        .existsByProjectIdAndUserId(
                                project.getId(),
                                user.getId()
                        );

        if (!owner && !member) {

            throw new AccessDeniedException(
                    "You are not a member of this project"
            );
        }
    }


    private void checkProjectOwner(
            Project project,
            User user
    ) {

        if (!project.getOwner()
                .getId()
                .equals(user.getId())) {

            throw new AccessDeniedException(
                    "Only the project owner can perform this action"
            );
        }
    }
}