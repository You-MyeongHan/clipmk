package com.bayclip.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayclip.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
	List<ChatRoom> findByUsers_Id(Integer userId);
}
