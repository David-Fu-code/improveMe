package com.drikek.improveMe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService {

    private final ChatClient chatClient;

    public String generateImprovementPlan(String assessmentData, String category, String userGoals) {

        String prompt = "Genera un plan de mejora personalizado basado en: " +
                "Assessment: " + assessmentData +
                "Categoría: " + category +
                "Objetivos: " + userGoals;

        return chatClient.prompt().user(prompt).call().content();
    }
}
