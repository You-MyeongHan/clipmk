package com.infra.meta.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ItemIdDto {
	private long id;
	private LocalDateTime  wr_date;
	
	public ItemIdDto(long id, LocalDateTime wr_date) {
	    this.id = id;
	    this.wr_date = wr_date;
	    }
	
}
