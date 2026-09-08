package com.forgeflow.repository;

import com.forgeflow.entity.Activity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository
        extends JpaRepository<Activity, Long> {

    List<Activity> findByProjectIdOrderByCreatedAtDesc(
            Long projectId,
            Pageable pageable
    );
}