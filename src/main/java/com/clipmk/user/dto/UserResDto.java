package com.clipmk.user.dto;

import org.springframework.data.domain.Page;

import com.clipmk.barter.dto.ItemsResDto;
import com.clipmk.board.dto.CommentsResDto;
import com.clipmk.board.dto.PostsResDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResDto {
	private UserInfoDto user;
	private Page<PostsResDto> posts;
	private Page<CommentsResDto> comments;
	private Page<ItemsResDto> items;
}
