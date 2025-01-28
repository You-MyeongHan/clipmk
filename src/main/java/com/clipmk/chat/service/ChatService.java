package com.clipmk.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.clipmk.chat.dto.ChatMessageDto;
import com.clipmk.chat.dto.ChatRoomDto;
import com.clipmk.chat.entity.ChatMessage;
import com.clipmk.chat.entity.ChatRoom;
import com.clipmk.chat.repository.ChatMessageRepository;
import com.clipmk.chat.repository.ChatRoomRepository;
import com.clipmk.user.entity.User;
import com.clipmk.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    // private final ChatRoomRepository chatRoomRepository;
    // private final ChatMessageRepository chatMessageRepository;
    // private final SimpMessagingTemplate messagingTemplate;
    // private final UserService userService;
    
    // public ChatRoom createChatRoom(Integer userId1, Integer userId2) {
    // 	User user1 = userService.loadUserByUsername(userId1.toString());
    //     User user2 = userService.loadUserByUsername(userId2.toString());
        
    //     ChatRoom chatRoom = new ChatRoom();
    //     chatRoom.getUsers().add(user1);
    //     chatRoom.getUsers().add(user2);
        
    // 	chatRoomRepository.save(chatRoom);
    //     return chatRoom;
    // }
    
    // public List<ChatRoomDto> getAllChatRoomsDto(Integer userId) {
    // 	List<ChatRoom> chatRooms = chatRoomRepository.findByUsers_Id(userId);
    // 	List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
    	
    // 	for (ChatRoom chatRoom : chatRooms) {
    // 		ChatMessage lastMessage=chatMessageRepository.findFirstByChatRoomOrderByCreatedAtDesc(chatRoom);

    // 		ChatRoomDto chatRoomDto = ChatRoomDto.builder()
    // 				.roomId(chatRoom.getId())
    // 				.lastMessage(lastMessage.getContent())
    // 				.build();
    // 		for (User user : chatRoom.getUsers()) {
    //             if (user.getId() != userId) {
    //             	chatRoomDto.setReceiverNick(user.getNick());
    //             	chatRoomDto.setReceiverId(user.getId());
    //             }
    //         }
    // 		chatRoomDtos.add(chatRoomDto);
    // 	}
    // 	return chatRoomDtos;
    // }
    
    // public void sendMessage(WebSocketSession session, ChatMessage message) {
    //     try {
    //         // WebSocket 세션에 메시지를 전송합니다.
    //         messagingTemplate.convertAndSendToUser(session.getId(), "/sub/messages", message);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
    
    // public List<ChatRoom> getAllChatRooms() {
    //     return chatRoomRepository.findAll();
    // }

    // public List<ChatMessageDto> getChatHistory(ChatRoom chatRoom) {
    // 	List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom(chatRoom);
    	
    // 	List<ChatMessageDto> chatMessageDtos = chatMessages.stream()
    //             .map(ChatMessage::toDto)
    //             .collect(Collectors.toList());
    	
    //     return chatMessageDtos;
    // }

    // public void saveChatMessage(ChatMessage message) {
    //     chatMessageRepository.save(message);
    // }
}
