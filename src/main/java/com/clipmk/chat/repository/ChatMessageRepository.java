package com.clipmk.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clipmk.chat.entity.ChatMessage;
import com.clipmk.chat.entity.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{
	List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
	ChatMessage findFirstByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
