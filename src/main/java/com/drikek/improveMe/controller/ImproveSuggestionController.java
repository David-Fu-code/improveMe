package com.drikek.improveMe.controller;

import com.drikek.improveMe.dto.ImprovementSuggestionDTO;
import com.drikek.improveMe.entity.ImprovementSuggestion;
import com.drikek.improveMe.service.ImprovementSuggestionService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
@RequiredArgsConstructor
public class ImproveSuggestionController {

    private final ImprovementSuggestionService suggestionService;

    // Create new suggestion for a category
    @PostMapping("/{categoryId}")
    public ResponseEntity<@NonNull ImprovementSuggestionDTO> create(@PathVariable Long categoryId, @Valid @RequestBody ImprovementSuggestionDTO dto) {
        ImprovementSuggestionDTO created = suggestionService.createSuggestion(
                categoryId,
                dto.getTitle(),
                dto.getDescription(),
                dto.getDifficulty(),
                dto.getFrequency()
        );

        return ResponseEntity.ok(created);
    }

    // Get suggestions by category
    @GetMapping("/{categoryId}")
    public ResponseEntity<@NonNull List<ImprovementSuggestionDTO>> getBySuggestion(@PathVariable Long categoryId) {
        List<ImprovementSuggestionDTO> suggestionDTOS = suggestionService.getSuggestionsByCategory(categoryId);
        return ResponseEntity.ok(suggestionDTOS);
    }

    // Update a suggestion
    @PutMapping("/{suggestionId}")
    public ResponseEntity<@NonNull ImprovementSuggestionDTO> update(@PathVariable Long suggestionId, @Valid @RequestBody ImprovementSuggestionDTO dto) {
        ImprovementSuggestionDTO updated = suggestionService.updateSuggestion(
                suggestionId,
                dto.getTitle(),
                dto.getDescription(),
                dto.getDifficulty(),
                dto.getFrequency()
        );

        return ResponseEntity.ok(updated);
    }

    // Delete a suggestion
    @DeleteMapping("/{suggestionId}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable Long suggestionId) {
        suggestionService.deleteSuggestion(suggestionId);
        return ResponseEntity.noContent().build();
    }
}
