package com.clipmk.barter.dto;

import java.time.LocalDateTime;

import com.clipmk.barter.entity.Deal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DealsResDto {
	private Long id;
	private String title;
	private LocalDateTime  wr_date;
	private String nick;
	
	public static DealsResDto from(Deal deal) {
		
		DealsResDto dealsResDto = DealsResDto.builder()
				.id(deal.getId())
				.title(deal.getFromItem().getTitle())
				.wr_date(deal.getWr_date())
				.nick(deal.getFromUser().getNick())
				.build();
		return dealsResDto;
	}
}
