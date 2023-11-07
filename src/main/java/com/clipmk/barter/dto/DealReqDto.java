package com.clipmk.barter.dto;

import lombok.Data;

@Data
public class DealReqDto {
	private long fromItemId;
	private long toItemId;
	private String content;
	private Integer toUserId;
}