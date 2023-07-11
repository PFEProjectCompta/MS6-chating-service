package com.ges.chatingservice.dto;

import com.ges.chatingservice.model.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author WIAM
 **/
@AllArgsConstructor @NoArgsConstructor @Builder@Data
public class ChatMessageDTO {

//    private String id;
//    private String chatId;
    private String senderId;
    private String recipientId;
    private String senderName;
    private String recipientName;
    private String content;
    private Date timestamp;
    private MessageStatus status;
    private Long nombre_message_unread;
}
