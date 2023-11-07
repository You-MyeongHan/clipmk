package com.clipmk.barter.dto;

import java.time.LocalDateTime;

import com.clipmk.barter.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ItemsResDto {
	private String title;
	private Long id;
	private String nick;
	private String content;
	private String category;
	private String img_src;
	private LocalDateTime wr_date;
	private Integer view_cnt;
	private int deals_cnt;
	private Integer dib_cnt;
	private boolean dib_state;
	
	public static ItemsResDto from(Item item) {
		ItemsResDto itemsResponseDto = ItemsResDto.builder()
				.title(item.getTitle())
				.id(item.getId())
				.nick(item.getUser().getNick())
				.category(item.getCategory())
				.wr_date(item.getWr_date())
				.view_cnt(item.getViewCnt())
				.dib_cnt(item.getDib().size())
				.build();
		
		if (item.getImg_src() != null && !item.getImg_src().isEmpty()) {
            itemsResponseDto.setImg_src(item.getImg_src().get(0));
        }
		
        itemsResponseDto.setDeals_cnt(item.getSuggestedDeals().size());
		
		return itemsResponseDto;
	}
}