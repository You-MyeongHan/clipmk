package com.clipmk.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto{
	private Long postId;
	private String title;
	private String category;
	private String subCategory;
	private String content;
	private String thumbnail;
	private LocalDateTime  wrDate;
	private LocalDateTime  reDate;
	private LocalDateTime  delDate;
	private Integer viewCnt;
	private String userNick;
	private Integer userId;
	private int recommendCnt;
	private int decommendCnt;
	private int recommendState;
	
	@OrderBy("id desc")
	private List<CommentDto> comments;
}