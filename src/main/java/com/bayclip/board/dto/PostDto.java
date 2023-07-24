package com.bayclip.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.bayclip.board.entity.Comment;

import jakarta.persistence.OrderBy;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
	private String nick;
	private final Integer recommend_cnt;
	private final Integer decommend_cnt;
	private final boolean recommend_state;
	private final boolean decommend_state;
	
	@OrderBy("id desc")
	private List<Comment> comments;
}