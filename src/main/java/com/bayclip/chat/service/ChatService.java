package com.bayclip.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bayclip.barter.entity.Deal;
import com.bayclip.chat.entity.ChatMessage;
import com.bayclip.chat.entity.ChatRoom;
import com.bayclip.chat.repository.ChatMessageRepository;
import com.bayclip.chat.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public long createChatRoom(Deal deal) {
    	
    	ChatRoom chatRoom =ChatRoom.builder()
    			.deal(deal)
    			.build();
    	chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
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
