package com.bayclip.board.dto;

import lombok.Data;

@Data
public class CommentCreateDto {
	private String content;
	private Long postId;
}
