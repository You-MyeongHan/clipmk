package com.bayclip.barter.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
	private Long id;
	private String title;
	private String category;
	private String content;
	private LocalDateTime  wr_date;
	private LocalDateTime  re_date;
	private LocalDateTime  del_date;
	private Integer viewCnt;
}
