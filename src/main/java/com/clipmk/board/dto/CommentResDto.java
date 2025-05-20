package com.clipmk.board.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResDto {
	private Long commentId;
	private String content;
	private LocalDateTime  wrDate;
	private LocalDateTime  delDate;
	private String nick;
	private Long parentCommentId;
	// private List<CommentResDto> replies;
}
