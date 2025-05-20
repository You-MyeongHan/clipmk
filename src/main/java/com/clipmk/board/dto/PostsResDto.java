package com.clipmk.board.dto;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.clipmk.barter.dto.ItemsResDto;
import com.clipmk.board.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsResDto {
	private Long postId;
	private String title;
	private String category;
	private String subCategory;
	private String nick;
	private LocalDateTime wrDate;
	private Integer viewCnt;
	private Integer recommendCnt;
	private Integer commentCnt;
	private String thumbnail;

	public static PostsResDto from(Post post) {
		PostsResDto postsResDto = PostsResDto.builder()
				.postId(post.getPostId())
				.title(post.getTitle())
				.nick(post.getUser().getNick())
				.category(post.getCategory())
				.subCategory(post.getSubCategory())
				.wrDate(post.getFrsRgDtm())
				.viewCnt(post.getViewCnt())
				.commentCnt(post.getComments().size())
				.thumbnail(post.getThumbnail())
				.build();
		return postsResDto;
	}
}