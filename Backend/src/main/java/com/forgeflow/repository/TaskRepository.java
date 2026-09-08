package com.forgeflow.repository;

import com.forgeflow.entity.Task;
import com.forgeflow.entity.TaskPriority;
import com.forgeflow.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // =========================================================
    // PROJECT TASKS
    // =========================================================

    List<Task> findByProjectId(
            Long projectId,
            Sort sort
    );

    Page<Task> findByProjectId(
            Long projectId,
            Pageable pageable
    );


    // =========================================================
    // FILTER BY STATUS
    // =========================================================

    List<Task> findByProjectIdAndStatus(
            Long projectId,
            TaskStatus status,
            Sort sort
    );

    Page<Task> findByProjectIdAndStatus(
            Long projectId,
            TaskStatus status,
            Pageable pageable
    );


    // =========================================================
    // FILTER BY PRIORITY
    // =========================================================

    List<Task> findByProjectIdAndPriority(
            Long projectId,
            TaskPriority priority,
            Sort sort
    );

    Page<Task> findByProjectIdAndPriority(
            Long projectId,
            TaskPriority priority,
            Pageable pageable
    );


    // =========================================================
    // FILTER BY STATUS + PRIORITY
    // =========================================================

    List<Task> findByProjectIdAndStatusAndPriority(
            Long projectId,
            TaskStatus status,
            TaskPriority priority,
            Sort sort
    );

    Page<Task> findByProjectIdAndStatusAndPriority(
            Long projectId,
            TaskStatus status,
            TaskPriority priority,
            Pageable pageable
    );


    // =========================================================
    // ASSIGNED TASKS
    // =========================================================

    List<Task> findByAssignedUserId(
            Long userId,
            Sort sort
    );

    Page<Task> findByAssignedUserId(
            Long userId,
            Pageable pageable
    );

    // =========================================================
// DASHBOARD COUNT QUERIES
// =========================================================

    long countByProjectId(
            Long projectId
    );


    long countByProjectIdAndStatus(
            Long projectId,
            TaskStatus status
    );


    long countByProjectIdAndPriority(
            Long projectId,
            TaskPriority priority
    );


    long countByProjectIdAndAssignedUserIsNotNull(
            Long projectId
    );


    long countByProjectIdAndAssignedUserIsNull(
            Long projectId
    );
}