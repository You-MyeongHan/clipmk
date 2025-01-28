package com.clipmk.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResDto {
	private String accessToken;
	private String refreshToken;
	private Integer id;
	private String nick;
	private String email;
	private Integer point;
}
