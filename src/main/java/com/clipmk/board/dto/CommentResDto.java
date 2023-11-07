package com.clipmk.board.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResDto {
	private Long id;
	private String content;
	private LocalDateTime  wr_date;
	private LocalDateTime  del_date;
	private String nick;
	private Long parentCommentId;
}
