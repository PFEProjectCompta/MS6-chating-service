package com.ges.chatingservice.controller;

import com.ges.chatingservice.dto.ChatMessageDTO;
import com.ges.chatingservice.model.ChatMessage;
import com.ges.chatingservice.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WIAM
 **/
@RestController
public class ChatGraphQLController {
    @Autowired
    private ChatMessageService chatMessageService;
    @QueryMapping
    public List<ChatMessage> findChatMessages (@Argument String senderId,
                                               @Argument String recipientId) {
        return chatMessageService.findChatMessages(senderId, recipientId);
    }
    @QueryMapping
    public Long countNewMessages (@Argument String senderId,
                                               @Argument String recipientId) {
        return chatMessageService.countNewMessages(senderId,recipientId);
    }

    @QueryMapping
    public List<ChatMessageDTO> newMessages(@Argument String reciver) {
        return chatMessageService.newMessages(reciver);
    }
}
