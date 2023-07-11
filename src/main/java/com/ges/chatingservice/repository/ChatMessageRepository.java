package com.ges.chatingservice.repository;

import com.ges.chatingservice.model.ChatMessage;
import com.ges.chatingservice.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author WIAM
 **/

public interface ChatMessageRepository
        extends MongoRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);
    List<ChatMessage> findByRecipientId(String senderId);
}
