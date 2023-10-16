package com.bayclip.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendCntResponseDto {
	private int recommend_cnt;
	private int decommend_cnt;
	private int recommend_state;
}
