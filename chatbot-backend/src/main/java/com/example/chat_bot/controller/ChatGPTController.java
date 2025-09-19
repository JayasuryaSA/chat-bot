package com.example.chat_bot.controller;

import com.example.chat_bot.dto.PromptRequest;
import com.example.chat_bot.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/chat")
public class ChatGPTController {
    @Autowired
    private ChatGPTService chatGPTService;

    @PostMapping
    public String chat(@RequestBody PromptRequest promptRequest){
        return chatGPTService.getChatResponse(promptRequest);
    }
}
