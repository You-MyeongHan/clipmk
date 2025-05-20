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
	private Long commentId;
	private String content;
	private LocalDateTime frsRgDtm;
	private LocalDateTime lstChDtm;
	private Long recommendCnt;
	
	public static CommentsResDto from(Comment comment) {
		CommentsResDto commentsResDto = CommentsResDto.builder()
				.commentId(comment.getCommentId())
				.content(comment.getContent())
				.frsRgDtm(comment.getFrsRgDtm())
				.lstChDtm(comment.getLastChDtm())
				.recommendCnt(comment.getRecommendCount())
				.build();
		return commentsResDto; 
	}
}
