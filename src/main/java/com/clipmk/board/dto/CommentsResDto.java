package com.clipmk.board.dto;

import java.time.LocalDateTime;

import com.clipmk.board.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentsResDto {
	private Long id;
	private Long postId;
	private String content;
	private LocalDateTime  wr_date;
	private Integer recommend_cnt;
	
	public static CommentsResDto from(Comment comment) {
		CommentsResDto commentsResDto = CommentsResDto.builder()
				.id(comment.getId())
				.postId(comment.getPost().getId())
				.content(comment.getContent())
				.wr_date(comment.getWr_date())
				.recommend_cnt(comment.getRecommendations().size())
				.build();
		return commentsResDto; 
	}
}
