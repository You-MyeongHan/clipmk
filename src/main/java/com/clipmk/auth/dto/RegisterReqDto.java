package com.clipmk.auth.dto;

import com.clipmk.user.dto.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReqDto {
	private String uid;
	private String pwd;
	private String nick;
	private String email;
	private Boolean emailReceive;
	private Role role;
}
