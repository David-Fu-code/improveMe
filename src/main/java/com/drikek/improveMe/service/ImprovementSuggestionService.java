package com.drikek.improveMe.service;

import com.drikek.improveMe.dto.ImprovementSuggestionDTO;
import com.drikek.improveMe.entity.Category;
import com.drikek.improveMe.entity.ImprovementSuggestion;
import com.drikek.improveMe.exception.AuthException;
import com.drikek.improveMe.repository.CategoryRepository;
import com.drikek.improveMe.repository.ImprovementSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImprovementSuggestionService {

    private final ImprovementSuggestionRepository suggestionRepository;
    private final CategoryRepository categoryRepository;

    // Map to DTO
    private ImprovementSuggestionDTO mapToDTO(ImprovementSuggestion suggestion) {
        ImprovementSuggestionDTO dto = new ImprovementSuggestionDTO();
        dto.setId(suggestion.getId());
        dto.setTitle(suggestion.getTitle());
        dto.setDescription(suggestion.getDescription());
        dto.setDifficulty(suggestion.getDifficulty());
        dto.setFrequency(suggestion.getFrequency());

        return dto;
    }

    // Create suggestion
    public ImprovementSuggestionDTO createSuggestion(Long categoryId, String title,
                                                  String description, String difficulty, String frequency) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AuthException("Category not found", 404));

        ImprovementSuggestion suggestion = new ImprovementSuggestion();
        suggestion.setCategory(category);
        suggestion.setTitle(title);
        suggestion.setDescription(description);
        suggestion.setDifficulty(difficulty);
        suggestion.setFrequency(frequency);

        ImprovementSuggestion saved = suggestionRepository.save(suggestion);

        return mapToDTO(saved);
    }
    // Update suggestion
    public ImprovementSuggestionDTO updateSuggestion(Long suggestionId, String title, String description, String difficulty, String frequency) {

        ImprovementSuggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new AuthException("Category not found", 404));

        if (title != null && !title.isBlank()){suggestion.setTitle(title);}
        if (description != null && !description.isBlank()){suggestion.setDescription(description);}
        if (difficulty != null && !difficulty.isBlank()){suggestion.setDifficulty(difficulty);}
        if (frequency != null && !frequency.isBlank()){suggestion.setFrequency(frequency);}

        ImprovementSuggestion saved = suggestionRepository.save(suggestion);

        return mapToDTO(saved);
    }

    // Get suggestion by category
    public List<ImprovementSuggestionDTO> getSuggestionsByCategory(Long categoryId) {
        return suggestionRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // Delete suggestion
    public void deleteSuggestion(Long id) {
        suggestionRepository.deleteById(id);
    }

}
