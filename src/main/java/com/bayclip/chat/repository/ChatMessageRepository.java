package com.bayclip.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayclip.chat.entity.ChatMessage;
import com.bayclip.chat.entity.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{
	List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
	ChatMessage findFirstByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
