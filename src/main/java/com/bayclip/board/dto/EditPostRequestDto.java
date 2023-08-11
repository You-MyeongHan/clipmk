package com.bayclip.board.dto;

import lombok.Data;

@Data
public class EditPostRequestDto {
	private String title;
	private String category;
	private String content;
	private String thumbnail;
}
