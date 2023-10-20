package com.bayclip.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostReqDto {
	private String title;
	private String table;
	private String group;
	private String content;
	private String thumbnail;
}