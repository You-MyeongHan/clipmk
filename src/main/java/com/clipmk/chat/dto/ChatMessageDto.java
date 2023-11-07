package com.clipmk.chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
	private Integer senderId;
	private Integer receiverId;
	private String receiverNick;
	private String content;
	private MessageType type;
	private LocalDateTime createdAt;
}
