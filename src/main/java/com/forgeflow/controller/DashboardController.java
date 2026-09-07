package com.forgeflow.controller;

import com.forgeflow.dto.ProjectStatsResponse;
import com.forgeflow.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;


    @GetMapping
    public ResponseEntity<ProjectStatsResponse> getProjectStats(
            @PathVariable Long projectId,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                dashboardService.getProjectStats(
                        projectId,
                        email
                )
        );
    }
}