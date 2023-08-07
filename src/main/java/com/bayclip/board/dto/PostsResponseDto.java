package com.bayclip.board.dto;

import java.time.LocalDateTime;

import com.bayclip.board.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostsResponseDto {
	private String title;
	private Long id;
	private String nick;
	private String category;
	private LocalDateTime wr_date;
	private Integer view_cnt;
	private Integer recommend_cnt;

	
	public static PostsResponseDto from(Post post) {
		return new PostsResponseDto(post);
	}
	
	private PostsResponseDto(Post post) {
		this.title=post.getTitle();
		this.nick=post.getNick();
		this.id=post.getId();
		this.category=post.getCategory();
		this.wr_date=post.getWr_date();
		this.view_cnt=post.getViewCnt();
		this.recommend_cnt=post.getRecommendations().size();
	}
}