package com.clipmk.board.dto;

import lombok.Data;

@Data
public class EditPostReqDto {
	private String title;
	private String category;
	private String subCategory;
	private String content;
	private String thumbnail;
}
