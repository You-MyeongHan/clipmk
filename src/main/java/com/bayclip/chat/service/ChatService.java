package com.bayclip.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.bayclip.chat.dto.ChatRoomDto;
import com.bayclip.chat.entity.ChatMessage;
import com.bayclip.chat.entity.ChatRoom;
import com.bayclip.chat.repository.ChatMessageRepository;
import com.bayclip.chat.repository.ChatRoomRepository;
import com.bayclip.user.entity.User;
import com.bayclip.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    
    public ChatRoom createChatRoom(Integer userId1, Integer userId2) {
    	User user1 = userService.loadUserByUsername(userId1.toString());
        User user2 = userService.loadUserByUsername(userId2.toString());
        
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.getUsers().add(user1);
        chatRoom.getUsers().add(user2);
        
    	chatRoomRepository.save(chatRoom);
        return chatRoom;
    }
    
    public List<ChatRoomDto> getAllChatRoomsDto() {
    	List<ChatRoom> chatRooms = chatRoomRepository.findAll();
    	List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
    	
    	for (ChatRoom chatRoom : chatRooms) {
    		ChatMessage lastMessage=chatMessageRepository.findFirstByChatRoomOrderByCreatedAtDesc(chatRoom);
    		ChatRoomDto chatRoomDto = ChatRoomDto.builder()
    				.id(chatRoom.getId())
    				.lastMessage(lastMessage)
    				.partnerNickname("notYet")
    				.build();
    		chatRoomDtos.add(chatRoomDto);
    	}
    	return chatRoomDtos;
    }
    
    public void sendMessage(WebSocketSession session, ChatMessage message) {
        try {
            // WebSocket 세션에 메시지를 전송합니다.
            messagingTemplate.convertAndSendToUser(session.getId(), "/sub/messages", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public List<ChatMessage> getChatHistory(ChatRoom chatRoom) {
        return chatMessageRepository.findByChatRoom(chatRoom);
    }

    public void saveChatMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }
}
