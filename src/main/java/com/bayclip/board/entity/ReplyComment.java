package com.bayclip.board.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.bayclip.board.dto.ReplyCommentDto;
import com.bayclip.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@Table(name = "reply_comment")
public class ReplyComment {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private String content;
	
	@CreationTimestamp
	private LocalDateTime  wr_date;
	private LocalDateTime  del_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	@JsonIgnore
    private User user;
	
	private String nick;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
	@JsonIgnore
    private Post post;
	
	@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment parentComment;
	
	public ReplyCommentDto toDto() {
		
		ReplyCommentDto replyCommentDto = ReplyCommentDto.builder()
			.id(this.id)
			.content(this.content)
			.wr_date(this.wr_date)
			.del_date(this.del_date)
			.user_id(this.user.getId())
			.user_nick(this.user.getNick())
			.build();
		
		return replyCommentDto;
	}
			
}
