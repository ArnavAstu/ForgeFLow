package com.forgeflow.controller;

import com.forgeflow.dto.AddMemberRequest;
import com.forgeflow.dto.ProjectMemberResponse;
import com.forgeflow.dto.UpdateMemberRoleRequest;
import com.forgeflow.service.ProjectMemberService;
import jakarta.validation.Valid;
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


    @PostMapping
    public ResponseEntity<ProjectMemberResponse> addMember(
            @PathVariable Long projectId,

            @Valid
            @RequestBody
            AddMemberRequest request,

            Authentication authentication
    ) {

        ProjectMemberResponse response =
                projectMemberService.addMember(
                        projectId,
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getMembers(
            @PathVariable Long projectId,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                projectMemberService.getMembers(
                        projectId,
                        authentication.getName()
                )
        );
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            Authentication authentication
    ) {

        projectMemberService.removeMember(
                projectId,
                userId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{userId}/role")
    public ResponseEntity<ProjectMemberResponse> updateRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,

            @Valid
            @RequestBody
            UpdateMemberRoleRequest request,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                projectMemberService.updateRole(
                        projectId,
                        userId,
                        request,
                        authentication.getName()
                )
        );
    }
}