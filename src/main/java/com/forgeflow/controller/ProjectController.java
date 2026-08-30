package com.forgeflow.controller;

import com.forgeflow.dto.CreateProjectRequest;
import com.forgeflow.dto.ProjectResponse;
import com.forgeflow.dto.UpdateProjectRequest;
import com.forgeflow.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;


    // =========================================================
    // CREATE PROJECT
    // =========================================================

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        ProjectResponse response =
                projectService.createProject(
                        request,
                        email
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET PROJECT
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                projectService.getProject(id)
        );
    }


    // =========================================================
    // GET ALL PROJECTS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {

        return ResponseEntity.ok(
                projectService.getAllProjects()
        );
    }


    // =========================================================
    // GET MY PROJECTS
    // =========================================================

    @GetMapping("/my")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                projectService.getMyProjects(email)
        );
    }


    // =========================================================
    // UPDATE PROJECT
    // =========================================================

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        ProjectResponse response =
                projectService.updateProject(
                        id,
                        request,
                        email
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // DELETE PROJECT
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        projectService.deleteProject(
                id,
                email
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}