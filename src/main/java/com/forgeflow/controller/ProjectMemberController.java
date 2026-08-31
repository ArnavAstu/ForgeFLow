package com.forgeflow.controller;

import com.forgeflow.dto.ProjectMemberResponse;
import com.forgeflow.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/members")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;


    // ADD MEMBER
    @PostMapping("/{userId}")
    public ResponseEntity<ProjectMemberResponse> addMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            Authentication authentication
    ) {

        String ownerEmail =
                authentication.getName();

        ProjectMemberResponse response =
                projectMemberService.addMember(
                        projectId,
                        userId,
                        ownerEmail
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // GET MEMBERS
    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getMembers(
            @PathVariable Long projectId
    ) {

        return ResponseEntity.ok(
                projectMemberService.getMembers(
                        projectId
                )
        );
    }


    // REMOVE MEMBER
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            Authentication authentication
    ) {

        String ownerEmail =
                authentication.getName();

        projectMemberService.removeMember(
                projectId,
                userId,
                ownerEmail
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}