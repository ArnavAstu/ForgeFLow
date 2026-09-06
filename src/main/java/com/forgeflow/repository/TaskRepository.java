package com.forgeflow.repository;

import com.forgeflow.entity.Task;
import com.forgeflow.entity.TaskPriority;
import com.forgeflow.entity.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository
        extends JpaRepository<Task, Long> {

    // =========================================================
    // PROJECT TASKS
    // =========================================================

    List<Task> findByProjectId(
            Long projectId
    );


    List<Task> findByProjectId(
            Long projectId,
            Sort sort
    );


    // =========================================================
    // STATUS FILTER
    // =========================================================

    List<Task> findByProjectIdAndStatus(
            Long projectId,
            TaskStatus status,
            Sort sort
    );


    // =========================================================
    // PRIORITY FILTER
    // =========================================================

    List<Task> findByProjectIdAndPriority(
            Long projectId,
            TaskPriority priority,
            Sort sort
    );


    // =========================================================
    // STATUS + PRIORITY
    // =========================================================

    List<Task> findByProjectIdAndStatusAndPriority(
            Long projectId,
            TaskStatus status,
            TaskPriority priority,
            Sort sort
    );


    // =========================================================
    // TASKS ASSIGNED TO USER
    // =========================================================

    List<Task> findByAssignedUserId(
            Long userId
    );


    List<Task> findByAssignedUserId(
            Long userId,
            Sort sort
    );
}