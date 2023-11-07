package com.clipmk.barter.dto;

import lombok.Data;

@Data
public class DealAcceptReqDto {
	private long dealId;
	private Integer userId1;
	private Integer userId2;
}
