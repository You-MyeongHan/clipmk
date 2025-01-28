package com.clipmk.chat.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.clipmk.barter.entity.Deal;
import com.clipmk.chat.dto.ChatRoomDto;
import com.clipmk.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="chatRoom")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ChatRoom {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id")
	@JsonIgnore
	@Nullable
    private Deal deal;
	@CreationTimestamp
    private LocalDateTime createdAt;
	@ManyToMany
	@Builder.Default
    @JoinTable(
        name = "chat_room_user",
        joinColumns = @JoinColumn(name = "chat_room_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();
	
	public ChatRoomDto toDto(ChatMessage lastMessage) {
		ChatRoomDto chatRoomDto=ChatRoomDto.builder()
				.roomId(this.getId())
				.lastMessage(lastMessage.getContent())
				.receiverNick("notYet").build();
		return chatRoomDto;
	}
	
//	public void handleAction(WebSocketSession session, ChatMessage message, ChatService service) {
//		if(message.getType().equals(MessageType.ENTER)){
//			sessions.add(session);
//			message.setContent(message.getSender() + " 님이 입장하였습니다.");
//			sendMessage(message, service);
//		}else if (message.getType().equals(MessageType.SEND)) {
//            message.setContent(message.getContent());
//            sendMessage(message,service);
//        }
//		
//	}
//	
//	public <T> void sendMessage(T message, ChatService service){
//        sessions.parallelStream().forEach(sessions -> service.sendMessage(sessions,message));
//    }
}