package com.bayclip.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayclip.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{

}
