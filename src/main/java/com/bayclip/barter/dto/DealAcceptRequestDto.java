package com.bayclip.barter.dto;

import lombok.Data;

@Data
public class DealAcceptRequestDto {
	private long dealId;
	private Integer userId1;
	private Integer userId2;
}
