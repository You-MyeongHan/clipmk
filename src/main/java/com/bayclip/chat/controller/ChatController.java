package com.bayclip.chat.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bayclip.chat.dto.ChatMessageDto;
import com.bayclip.chat.dto.ChatRoomDto;
import com.bayclip.chat.dto.ChatRoomReqDto;
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
	
	@PostMapping("/room")
    public ResponseEntity<Void> createChatRoom(
    		@RequestBody ChatRoomReqDto request
    		) {
        ChatRoom chatRoom = chatService.createChatRoom(request.getUserId1(), request.getUserId2());
        if(chatRoom!=null) {
        	return ResponseEntity.ok().build();
        }
        else {
        	return ResponseEntity.badRequest().build();
        }
    }
	
	//chatRoom 페이징
    @GetMapping("/rooms/{user-id}")
    public ResponseEntity<List<ChatRoomDto>> getAllChatRooms(
    		@PathVariable("user-id") Integer userId
//    		@AuthenticationPrincipal User user
    	) {
    	
    	List<ChatRoomDto> chatRoomDtos=chatService.getAllChatRoomsDto(userId);
    	
        if(chatRoomDtos!=null) {
        	return ResponseEntity.ok(chatRoomDtos);
        }else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(
    		@RequestParam Long roomId,
    		@AuthenticationPrincipal User user 
    	) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        if (chatRoom != null) {
            return ResponseEntity.ok(chatService.getChatHistory(chatRoom));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/send-message")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ChatMessage> sendChatMessage(
    		@RequestBody ChatMessage message
//    		@AuthenticationPrincipal User user
    	) {
        // 메시지를 저장하고 필요에 따라 수신자에게 전송
        chatService.saveChatMessage(message);
        // 응답 처리
        return ResponseEntity.ok().build();
    }
}
