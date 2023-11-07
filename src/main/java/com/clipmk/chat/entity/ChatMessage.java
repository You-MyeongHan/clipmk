package com.clipmk.chat.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.clipmk.chat.dto.ChatMessageDto;
import com.clipmk.chat.dto.MessageType;
import com.clipmk.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="chatMessage")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ChatMessage {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private MessageType type;
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
    
    public ChatMessageDto toDto() {
    	ChatMessageDto chatMessageDto = ChatMessageDto.builder()
    			.senderId(this.sender.getId())
    			.receiverId(this.receiver.getId())
    			.receiverNick(this.receiver.getNick())
    			.content(this.content)
    			.createdAt(this.createdAt)
    			.build();
    	return chatMessageDto;
    }
}