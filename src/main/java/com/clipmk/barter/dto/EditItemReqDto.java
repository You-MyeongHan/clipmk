package com.clipmk.barter.dto;

import java.util.List;

import lombok.Data;

@Data
public class EditItemReqDto {
	private String title;
	private String category;
	private String content;
	private List<String> imgSrc;
}
