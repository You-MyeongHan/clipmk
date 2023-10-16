package com.infra.email.dto;

import lombok.Data;

@Data
public class EmailVerifyRequestDto {
	private String authCode;
	private String email;
}
