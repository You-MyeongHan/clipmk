package com.bayclip.board.dto;

import lombok.Data;

@Data
public class EditPostReqDto {
	private String title;
	private String category;
	private String content;
	private String thumbnail;
}
