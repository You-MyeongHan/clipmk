package com.bayclip.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {
	private String title;
	private String category;
	private String content;
	private String thumbnail;
}