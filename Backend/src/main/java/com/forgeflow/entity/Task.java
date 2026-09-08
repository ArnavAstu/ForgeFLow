package com.forgeflow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            nullable = false,
            length = 150
    )
    private String title;


    @Column(length = 3000)
    private String description;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;


    // =========================================================
    // DUE DATE
    // =========================================================

    private LocalDateTime dueDate;


    // =========================================================
    // TASK BELONGS TO ONE PROJECT
    // =========================================================

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "project_id",
            nullable = false
    )
    private Project project;


    // =========================================================
    // TASK CAN BE ASSIGNED TO ONE USER
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;


    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // =========================================================
    // BEFORE INSERT
    // =========================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        createdAt = now;

        updatedAt = now;


        if (status == null) {

            status =
                    TaskStatus.TODO;
        }


        if (priority == null) {

            priority =
                    TaskPriority.MEDIUM;
        }
    }


    // =========================================================
    // BEFORE UPDATE
    // =========================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}