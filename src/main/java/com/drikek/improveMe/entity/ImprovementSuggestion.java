package com.drikek.improveMe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "improvement_suggestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImprovementSuggestion {

    @Id
    @SequenceGenerator(sequenceName = "improvement_sequence", name = "improvement_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "improvement_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private String difficulty; // e.g., Easy, Medium, Hard

    private String frequency; // e.g., Daily, Weekly, Custom
}
