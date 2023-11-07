package com.clipmk.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserInfoDto {
	private Integer id;
	private String uid;
	private String nick;
	private String email;
	private Boolean emailReceive;
}
