package com.bayclip.user.dto;

import org.springframework.data.domain.Page;

import com.bayclip.board.entity.Comment;
import com.bayclip.board.entity.Post;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoDto {
	private Integer id;
	private String uid;
	private String nick;
	private String email;
	private Boolean emailReceive;
	private Page<Post> posts;
	private Page<Comment> comments;
}
