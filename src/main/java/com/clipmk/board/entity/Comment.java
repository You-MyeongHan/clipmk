package com.clipmk.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.clipmk.board.dto.CommentDto;
import com.clipmk.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "comment")
@DynamicInsert
@DynamicUpdate
public class Comment {
	@Id
	@Column(name = "COMMENT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	
	@Column(name = "CONTENT", nullable = false, length = 1000)
	private String content;
	
	@Column(name = "FRS_RG_GUID", nullable = false, length = 36)
	private String frsRgGuid;  // 최초 등록자 GUID
	
	@Column(name = "LST_CH_GUID", nullable = false, length = 36)
	private String lstChGuid;  // 최종 변경자 GUID
	
	@CreationTimestamp
	@Column(name = "FRS_RG_DTM", nullable = false)
	private LocalDateTime frsRgDtm;
	
	@CreationTimestamp
	@Column(name = "LAST_CH_DTM", nullable = false)
	private LocalDateTime lastChDtm;
	
	@Column(name = "DEL_YN")
	private String delYn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	@JsonIgnore
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Comment parent;
	
	@Builder.Default
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> replies = new ArrayList<>();
	
	// CommentReaction과 연관 관계
	@Builder.Default
	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CommentReaction> reactions = new ArrayList<>();
	
	@PrePersist
	protected void onCreate() {
		// 최초 등록 시 GUID 생성
		if (frsRgGuid == null) {
			frsRgGuid = UUID.randomUUID().toString();
		}
		// 최종 변경자 GUID도 최초 등록자와 동일하게 설정
		if (lstChGuid == null) {
			lstChGuid = frsRgGuid;
		}
	}
	
	@PreUpdate
	protected void onUpdate() {
		// 수정 시 최종 변경자 GUID 업데이트
		lstChGuid = UUID.randomUUID().toString();
	}
	
	// 추천/비추천 개수 구하는 헬퍼 메서드
	public long getRecommendCount() {
		return reactions.stream()
				.filter(r -> r.getType() == CommentReaction.ReactionType.UP)
				.count();
	}
	
	public long getDecommendCount() {
		return reactions.stream()
				.filter(r -> r.getType() == CommentReaction.ReactionType.DOWN)
				.count();
	}
	
	// 추천/비추천 상태 확인 메서드
	public int getRecommendState(User user) {
		if (user == null) return 0;
		
		return reactions.stream()
				.filter(r -> r.getUser().getId().equals(user.getId()))
				.findFirst()
				.map(r -> r.getType() == CommentReaction.ReactionType.UP ? 1 : -1)
				.orElse(0);
	}
	
	// 추천/비추천 추가/제거 메서드
	public void toggleReaction(User user, CommentReaction.ReactionType type) {
		reactions.stream()
				.filter(r -> r.getUser().getId().equals(user.getId()))
				.findFirst()
				.ifPresentOrElse(
					reaction -> {
						if (reaction.getType() == type) {
							// 같은 타입의 반응이면 제거
							reactions.remove(reaction);
						} else {
							// 다른 타입의 반응이면 타입 변경
							reaction.setType(type);
						}
					},
					() -> {
						// 새로운 반응 추가
						CommentReaction newReaction = CommentReaction.builder()
								.user(user)
								.comment(this)
								.type(type)
								.build();
						reactions.add(newReaction);
					}
				);
	}
	
	public List<CommentDto> getReplies(){
		List<CommentDto> replyDtos = new ArrayList<>();
        for (Comment reply : this.replies) {
        	CommentDto replyDto = CommentDto.from(reply);
            replyDto.setParent_nick(this.getUser().getNick());
            replyDtos.add(replyDto);
            replyDtos.addAll(reply.getReplies());
        }
        return replyDtos;
	}
	
}