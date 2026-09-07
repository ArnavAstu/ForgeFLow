package com.forgeflow.repository;

import com.forgeflow.entity.ProjectMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectMemberRepositoryTest {

    @Autowired
    private ProjectMemberRepository repository;


    // =========================================================
    // REPOSITORY SHOULD BE AVAILABLE
    // =========================================================

    @Test
    void repositoryShouldBeAvailable() {

        assertNotNull(repository);
    }


    // =========================================================
    // FIND MEMBER
    // =========================================================

    @Test
    void findByProjectIdAndUserIdShouldReturnEmptyForUnknownIds() {

        Optional<ProjectMember> result =
                repository.findByProjectIdAndUserId(
                        999999L,
                        999999L
                );

        assertTrue(result.isEmpty());
    }


    // =========================================================
    // CHECK MEMBER EXISTS
    // =========================================================

    @Test
    void existsByProjectIdAndUserIdShouldReturnFalseForUnknownIds() {

        boolean exists =
                repository.existsByProjectIdAndUserId(
                        999999L,
                        999999L
                );

        assertFalse(exists);
    }
}