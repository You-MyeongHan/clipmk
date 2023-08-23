package com.bayclip.chat.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bayclip.chat.entity.ChatMessage;
import com.bayclip.chat.entity.ChatRoom;
import com.bayclip.chat.repository.ChatRoomRepository;
import com.bayclip.chat.service.ChatService;
import com.bayclip.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;
	private final ChatRoomRepository chatRoomRepository;
	
//	@PostMapping("/room")
//	public ResponseEntity<Void> createRoom(){
//		
//	}
	
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> getAllChatRooms(
    		@AuthenticationPrincipal User user
    	) {
    	
    	List<ChatRoom> chatRoom=chatService.getAllChatRooms();
        if(chatRoom!=null) {
        	return ResponseEntity.ok(chatRoom);
        }
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
    		@RequestParam Long roomId,
    		@AuthenticationPrincipal User user
    	) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        if (chatRoom != null) {
            return ResponseEntity.ok(chatService.getChatHistory(chatRoom));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendChatMessage(
    		@RequestBody ChatMessage message,
    		@AuthenticationPrincipal User user
    	) {
        // 메시지를 저장하고 필요에 따라 수신자에게 전송
        chatService.saveChatMessage(message);
        // 응답 처리
        return ResponseEntity.ok().build();
    }
}
