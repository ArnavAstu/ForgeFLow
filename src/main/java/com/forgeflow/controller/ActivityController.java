package com.forgeflow.controller;

import com.forgeflow.dto.ActivityResponse;
import com.forgeflow.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;


    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getProjectActivity(
            @PathVariable Long projectId,

            @RequestParam(
                    defaultValue = "20"
            )
            int limit,

            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                activityService.getProjectActivity(
                        projectId,
                        email,
                        limit
                )
        );
    }
}