package com.drikek.improveMe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "assessment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assessment {

    @Id
    @SequenceGenerator(sequenceName = "assessment_sequence", name = "assessment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assessment_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questions; // JSON with evaluation questions

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answers; // JSON with evaluation answers

    @Column(nullable = false)
    private Integer score; // score (0-100)

    @Column(nullable = false, length = 50)
    private String level; // level (beginner, intermediate, advanced)

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
