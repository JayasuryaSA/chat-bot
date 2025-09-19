package com.example.chat_bot.service;

import com.example.chat_bot.dto.ChatGPTRequest;
import com.example.chat_bot.dto.ChatGPTResponse;
import com.example.chat_bot.dto.PromptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    @Autowired
    public ChatGPTService(RestClient restClient){
        this.restClient = restClient;
    }

    @Value("${openapi.api.key}")
    private String apiKey;

    @Value("${openapi.api.model}")
    private String model;

    public String getChatResponse(PromptRequest promptRequest) {
        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(model,
                List.of(new ChatGPTRequest.Message("user", promptRequest.prompt())));

        try {
            ChatGPTResponse response = restClient.post()
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(chatGPTRequest)
                    .retrieve()
                    .body(ChatGPTResponse.class);

            if (response.choices() == null || response.choices().isEmpty()) {
                throw new RuntimeException("No choices received from the API");
            }

            return response.choices().get(0).message().content();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get response from chat API: " + e.getMessage(), e);
        }
    }

}
