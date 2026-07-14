package com.drikek.improveMe.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("unused")
public class OpenAIConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        // Configure the chat client
        return builder.build();
    }
}
