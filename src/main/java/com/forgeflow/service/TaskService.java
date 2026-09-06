package com.forgeflow.service;

import com.forgeflow.dto.CreateTaskRequest;
import com.forgeflow.dto.TaskResponse;
import com.forgeflow.dto.UpdateTaskRequest;
import com.forgeflow.entity.Project;
import com.forgeflow.entity.Task;
import com.forgeflow.entity.TaskPriority;
import com.forgeflow.entity.TaskStatus;
import com.forgeflow.entity.User;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.ProjectMemberRepository;
import com.forgeflow.repository.ProjectRepository;
import com.forgeflow.repository.TaskRepository;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    // owner OR project member
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

        task.setDueDate(
                request.getDueDate()
        );

        task.setProject(project);


        Task savedTask =
                taskRepository.save(task);


        return new TaskResponse(
                savedTask
        );
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
    //
    // supports:
    // status
    // priority
    // sorting
    // =========================================================

    public List<TaskResponse> getProjectTasks(
            Long projectId,
            String userEmail,
            TaskStatus status,
            TaskPriority priority,
            String sortBy,
            String direction
    ) {

        Project project =
                getProject(projectId);

        User currentUser =
                getUserByEmail(userEmail);

        checkProjectAccess(
                project,
                currentUser
        );


        Sort sort =
                buildSort(
                        sortBy,
                        direction
                );


        List<Task> tasks;


        if (status != null &&
                priority != null) {

            tasks =
                    taskRepository
                            .findByProjectIdAndStatusAndPriority(
                                    projectId,
                                    status,
                                    priority,
                                    sort
                            );

        } else if (status != null) {

            tasks =
                    taskRepository
                            .findByProjectIdAndStatus(
                                    projectId,
                                    status,
                                    sort
                            );

        } else if (priority != null) {

            tasks =
                    taskRepository
                            .findByProjectIdAndPriority(
                                    projectId,
                                    priority,
                                    sort
                            );

        } else {

            tasks =
                    taskRepository
                            .findByProjectId(
                                    projectId,
                                    sort
                            );
        }


        return tasks
                .stream()
                .map(TaskResponse::new)
                .toList();
    }


    // =========================================================
    // MY ASSIGNED TASKS
    // =========================================================

    public List<TaskResponse> getMyTasks(
            String userEmail
    ) {

        User user =
                getUserByEmail(userEmail);


        Sort sort =
                Sort.by(
                        Sort.Direction.ASC,
                        "dueDate"
                );


        return taskRepository
                .findByAssignedUserId(
                        user.getId(),
                        sort
                )
                .stream()
                .map(TaskResponse::new)
                .toList();
    }


    // =========================================================
    // UPDATE TASK
    // owner OR project member
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


        if (request.getDueDate() != null) {

            task.setDueDate(
                    request.getDueDate()
            );
        }


        Task updatedTask =
                taskRepository.save(task);


        return new TaskResponse(
                updatedTask
        );
    }


    // =========================================================
    // DELETE TASK
    // owner only
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
    // owner only
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
                                task
                                        .getProject()
                                        .getId(),
                                userId
                        );


        boolean isOwner =
                task
                        .getProject()
                        .getOwner()
                        .getId()
                        .equals(userId);


        if (!isMember && !isOwner) {

            throw new AccessDeniedException(
                    "Task can only be assigned to the project owner or a project member"
            );
        }


        task.setAssignedUser(
                assignedUser
        );


        Task savedTask =
                taskRepository.save(task);


        return new TaskResponse(
                savedTask
        );
    }


    // =========================================================
    // UNASSIGN TASK
    // owner only
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


        return new TaskResponse(
                savedTask
        );
    }


    // =========================================================
    // BUILD SORT
    // =========================================================

    private Sort buildSort(
            String sortBy,
            String direction
    ) {

        String property =
                switch (sortBy) {

                    case "title" ->
                            "title";

                    case "priority" ->
                            "priority";

                    case "status" ->
                            "status";

                    case "createdAt" ->
                            "createdAt";

                    case "updatedAt" ->
                            "updatedAt";

                    default ->
                            "dueDate";
                };


        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(
                        direction
                )
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;


        return Sort.by(
                sortDirection,
                property
        );
    }


    // =========================================================
    // GET TASK ENTITY
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


    // =========================================================
    // GET PROJECT
    // =========================================================

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
            User user
    ) {

        boolean owner =
                project
                        .getOwner()
                        .getId()
                        .equals(
                                user.getId()
                        );


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


    // =========================================================
    // PROJECT OWNER ONLY
    // =========================================================

    private void checkProjectOwner(
            Project project,
            User user
    ) {

        if (!project
                .getOwner()
                .getId()
                .equals(
                        user.getId()
                )) {

            throw new AccessDeniedException(
                    "Only the project owner can perform this action"
            );
        }
    }
}