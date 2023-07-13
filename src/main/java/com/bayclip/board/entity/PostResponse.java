package com.bayclip.board.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class PostResponse {
	private final String title;
	private final Long id;
	private final String nick;
	private final String category;
	private final LocalDateTime wr_date;
	private final Integer view_cnt;
	private final int recommend;
	
	public static PostResponse from(Board board) {
		return new PostResponse(board);
	}
	
	private PostResponse(Board board) {
		this.title=board.getTitle();
		this.nick=board.getNick();
		this.id=board.getId();
		this.category=board.getCategory();
		this.wr_date=board.getWr_date();
		this.view_cnt=board.getViewCnt();
		this.recommend=board.getRecommend();
	}
}