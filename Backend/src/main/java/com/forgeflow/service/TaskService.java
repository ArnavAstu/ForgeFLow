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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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
    // Supports:
    // status
    // priority
    // sorting
    // pagination
    // =========================================================

    public Page<TaskResponse> getProjectTasks(
            Long projectId,
            String userEmail,
            TaskStatus status,
            TaskPriority priority,
            String sortBy,
            String direction,
            int page,
            int size
    ) {

        Project project =
                getProject(projectId);

        User currentUser =
                getUserByEmail(userEmail);

        checkProjectAccess(
                project,
                currentUser
        );


        // =====================================================
        // VALIDATE PAGINATION
        // =====================================================

        if (page < 0) {

            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {

            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }


        // =====================================================
        // BUILD SORT
        // =====================================================

        Sort sort =
                buildSort(
                        sortBy,
                        direction
                );


        // =====================================================
        // CREATE PAGE REQUEST
        // =====================================================

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );


        // =====================================================
        // QUERY
        // =====================================================

        Page<Task> tasks;


        if (status != null &&
                priority != null) {

            tasks =
                    taskRepository
                            .findByProjectIdAndStatusAndPriority(
                                    projectId,
                                    status,
                                    priority,
                                    pageable
                            );

        } else if (status != null) {

            tasks =
                    taskRepository
                            .findByProjectIdAndStatus(
                                    projectId,
                                    status,
                                    pageable
                            );

        } else if (priority != null) {

            tasks =
                    taskRepository
                            .findByProjectIdAndPriority(
                                    projectId,
                                    priority,
                                    pageable
                            );

        } else {

            tasks =
                    taskRepository
                            .findByProjectId(
                                    projectId,
                                    pageable
                            );
        }


        // =====================================================
        // ENTITY → DTO
        // =====================================================

        return tasks.map(
                TaskResponse::new
        );
    }


    // =========================================================
    // GET MY ASSIGNED TASKS
    //
    // NOW PAGINATED
    // =========================================================

    public Page<TaskResponse> getMyTasks(
            String userEmail,
            int page,
            int size
    ) {

        User user =
                getUserByEmail(userEmail);


        // =====================================================
        // VALIDATE PAGINATION
        // =====================================================

        if (page < 0) {

            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {

            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }


        // =====================================================
        // SORT BY DUE DATE
        // =====================================================

        Sort sort =
                Sort.by(
                        Sort.Direction.ASC,
                        "dueDate"
                );


        // =====================================================
        // CREATE PAGE REQUEST
        // =====================================================

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );


        // =====================================================
        // FETCH PAGINATED TASKS
        // =====================================================

        return taskRepository
                .findByAssignedUserId(
                        user.getId(),
                        pageable
                )
                .map(TaskResponse::new);
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


        // Find the user
        User assignedUser =
                userRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + userId
                                )
                        );


        // Check whether user belongs
        // to this project
        boolean isMember =
                projectMemberRepository
                        .existsByProjectIdAndUserId(
                                task.getProject().getId(),
                                userId
                        );


        // Project owner can also receive tasks
        boolean isOwner =
                task.getProject()
                        .getOwner()
                        .getId()
                        .equals(userId);


        // Reject outsiders
        if (!isMember && !isOwner) {

            throw new AccessDeniedException(
                    "Task can only be assigned to the project owner or a project member"
            );
        }


        // Assign validated user
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