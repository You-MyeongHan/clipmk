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
		PostsResponseDto postsResponseDto = PostsResponseDto.builder()
				.title(post.getTitle())
				.nick(post.getUser().getNick())
				.id(post.getId())
				.category(post.getCategory())
				.wr_date(post.getWr_date())
				.view_cnt(post.getViewCnt())
				.recommend_cnt(post.getRecommendations().size())
				.build();
		return postsResponseDto;
	}
}