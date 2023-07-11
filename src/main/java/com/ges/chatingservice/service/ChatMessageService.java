package com.ges.chatingservice.service;

import com.ges.chatingservice.dto.ChatMessageDTO;
import com.ges.chatingservice.exception.ResourceNotFoundException;
import com.ges.chatingservice.model.ChatMessage;
import com.ges.chatingservice.model.MessageStatus;
import com.ges.chatingservice.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WIAM
 **/
@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository repository;
    @Autowired private ChatRoomService chatRoomService;
    @Autowired private MongoOperations mongoOperations;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(String senderId, String recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }
    public List<ChatMessageDTO> newMessages(String reciver) {
        List<ChatMessage> messages=repository.findAll();
        List<ChatMessage> messages_all=new ArrayList<>();
        List<ChatMessageDTO> chatMessageDTOS =new ArrayList<>();
        for (int i=0;i<messages.size();i++){
            if(messages.get(i).getRecipientId()!=null){
                if(messages.get(i).getRecipientId().toString().equals(reciver)){
                    if(messages.get(i).getStatus()==MessageStatus.RECEIVED){
                        messages_all.add(messages.get(i));
                    }

                }
            }
        }
        for(int i=0;i<messages_all.size();i++){
            ChatMessageDTO chatMessageDTO=ChatMessageDTO.builder()
                    .senderId(messages_all.get(i).getSenderId())
                    .recipientId(messages_all.get(i).getRecipientId())
                    .senderName(messages_all.get(i).getSenderName())
                    .recipientName(messages_all.get(i).getRecipientName())
                    .content(messages_all.get(i).getContent())
                    .timestamp(messages_all.get(i).getTimestamp())
                    .status(messages_all.get(i).getStatus())
                    .nombre_message_unread(countNewMessages(messages_all.get(i).getSenderId(),messages_all.get(i).getRecipientId()))
                    .build();
            chatMessageDTOS.add(chatMessageDTO);
        }
        return chatMessageDTOS;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        System.out.println("hiiiii");
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);
        System.out.println(chatId);
        var messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());
        System.out.println(messages);
        if(messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(String id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }
}
