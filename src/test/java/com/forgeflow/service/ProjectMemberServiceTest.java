package com.forgeflow.service;

import com.forgeflow.dto.AddMemberRequest;
import com.forgeflow.entity.Project;
import com.forgeflow.entity.User;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.ProjectMemberRepository;
import com.forgeflow.repository.ProjectRepository;
import com.forgeflow.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectMemberServiceTest {

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectMemberService projectMemberService;


    // =========================================================
    // PROJECT DOES NOT EXIST
    // =========================================================

    @Test
    void shouldThrowWhenProjectDoesNotExist() {

        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        AddMemberRequest request =
                new AddMemberRequest();

        request.setEmail("test@example.com");

        assertThrows(
                ResourceNotFoundException.class,
                () -> projectMemberService.addMember(
                        1L,
                        request,
                        "owner@example.com"
                )
        );
    }


    // =========================================================
    // USER DOES NOT EXIST
    // =========================================================

    @Test
    void shouldThrowWhenUserDoesNotExist() {

        // Create owner
        User owner = new User();

        // IMPORTANT:
        // checkOwner() needs owner.getId()
        owner.setId(1L);


        // Create project with owner
        Project project = new Project();

        project.setOwner(owner);


        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));


        // The logged-in user is the owner
        when(userRepository.findByEmail("owner@example.com"))
                .thenReturn(Optional.of(owner));


        // Target member does not exist
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());


        AddMemberRequest request =
                new AddMemberRequest();

        request.setEmail("test@example.com");


        assertThrows(
                ResourceNotFoundException.class,
                () -> projectMemberService.addMember(
                        1L,
                        request,
                        "owner@example.com"
                )
        );
    }
}