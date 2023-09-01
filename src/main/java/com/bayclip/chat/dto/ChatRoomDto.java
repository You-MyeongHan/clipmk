package com.bayclip.chat.dto;

import com.bayclip.chat.entity.ChatMessage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomDto {
	private Long id;
	private ChatMessage lastMessage;
	private String partnerNickname;
}
