package com.bayclip.board.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentDto {
	private Long id;
	private String content;
	private LocalDateTime  wr_date;
	private LocalDateTime  del_date;
	private Integer user_id;
	private String user_nick;
}
