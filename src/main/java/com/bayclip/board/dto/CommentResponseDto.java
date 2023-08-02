package com.bayclip.board.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponseDto {
	private Long id;
	private String content;
	private LocalDateTime  wr_date;
	private LocalDateTime  del_date;
	private String nick;
	private Long parentCommentId;
}
