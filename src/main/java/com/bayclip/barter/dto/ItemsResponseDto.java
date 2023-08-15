package com.bayclip.barter.dto;

import java.time.LocalDateTime;

import com.bayclip.barter.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ItemsResponseDto {
	private String title;
	private Long id;
	private String nick;
	private String category;
	private LocalDateTime wr_date;
	private Integer view_cnt;
	private Integer interested_cnt;
	
	public static ItemsResponseDto from(Item item) {
		ItemsResponseDto itemsResponseDto = ItemsResponseDto.builder()
				.title(item.getTitle())
				.id(item.getId())
				.nick(item.getUser().getNick())
				.category(item.getCategory())
				.wr_date(item.getWr_date())
				.view_cnt(item.getViewCnt())
				.interested_cnt(item.getInterested().size())
				.build();
		return itemsResponseDto;
	}
}