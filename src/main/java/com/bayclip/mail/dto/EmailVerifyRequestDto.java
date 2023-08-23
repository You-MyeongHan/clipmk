package com.bayclip.mail.dto;

import lombok.Data;

@Data
public class EmailVerifyRequestDto {
	private String authCode;
	private String email;
}
