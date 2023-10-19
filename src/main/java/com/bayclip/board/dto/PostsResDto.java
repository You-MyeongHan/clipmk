package com.bayclip.board.dto;

import java.time.LocalDateTime;

import com.bayclip.board.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostsResDto {
	private String title;
	private Long id;
	private String nick;
	private String category;
	private LocalDateTime wr_date;
	private Integer view_cnt;
	private Integer recommend_cnt;
	private Integer comment_cnt;

	public static PostsResDto from(Post post) {
		PostsResDto postsResponseDto = PostsResDto.builder()
				.title(post.getTitle())
				.nick(post.getUser().getNick())
				.id(post.getId())
				.category(post.getCategory())
				.wr_date(post.getWr_date())
				.view_cnt(post.getViewCnt())
				.recommend_cnt(post.getRecommendations().size())
				.comment_cnt(post.getComments().size())
				.build();
		return postsResponseDto;
	}
}