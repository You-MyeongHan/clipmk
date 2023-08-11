package com.bayclip.board.dto;

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
	private Long id;
	private String title;
	private String category;
	private String content;
	private String thumbnail;
	private LocalDateTime  wr_date;
	private LocalDateTime  re_date;
	private LocalDateTime  del_date;
	private Integer viewCnt;
	private String user_nick;
	private Integer user_id;
	private int recommend_cnt;
	private int decommend_cnt;
	private int recommend_state;
	
	@OrderBy("id desc")
	private List<CommentDto> comments;
}