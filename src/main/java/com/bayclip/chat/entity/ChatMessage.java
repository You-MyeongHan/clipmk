package com.bayclip.chat.entity;

import java.awt.TrayIcon.MessageType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
	
	@Enumerated(EnumType.STRING)
	private MessageType messageType;
	private Long id;
    private String sender;
    private String content;
}