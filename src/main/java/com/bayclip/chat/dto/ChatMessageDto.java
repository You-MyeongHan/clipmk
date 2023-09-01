package com.bayclip.chat.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessageDto {
	private Long roomId;
	private String senderNick;
	private String content;
	private LocalDateTime timestamp;
}
