package com.clipmk.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomDto {
	private Long roomId;
	private Integer receiverId;
	private String lastMessage;
	private String receiverNick;
}
