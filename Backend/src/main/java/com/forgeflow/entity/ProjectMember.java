package com.forgeflow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "project_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"project_id", "user_id"}
                )
        }
)
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "project_id",
            nullable = false
    )
    private Project project;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false
    )
    private ProjectMemberRole role;
}