package com.clipmk.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.clipmk.chat.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/private-message")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto message){
		messagingTemplate.convertAndSendToUser(message.getReceiverId().toString(),"/private",message);
        System.out.println(message.toString());
        return message;
    }
}
//	@MessageMapping(value = "/chat/enter")
//	public void enter(ChatMessageDto message){
//	  message.setContent(message.getSenderNick() + "님이 채팅방에 참여하였습니다.");
//	  messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//	}
//    @MessageMapping(value = "/chat/message")
//    public void message(ChatMessageDto message){
//    	messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//    }
	
//	@MessageMapping("/chat")
//    public void sendMessage(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
//        messagingTemplate.convertAndSendToUser(
//            message.getReceiver().getId().toString(), // 수신자의 유저네임을 사용하여 특정 유저에게 전송
//            "/topic/messages",
//            message
//        );
//    }

