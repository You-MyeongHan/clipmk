package com.bayclip.barter.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.bayclip.barter.dto.ItemDto;
import com.bayclip.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="item")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String content;
	private String category;
	@ColumnDefault("0")
	@Column(name="view_cnt")
	private Integer viewCnt;
	@CreationTimestamp
	private LocalDateTime  wr_date;
	private LocalDateTime  re_date;
	private LocalDateTime  del_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	@JsonIgnore
    private User user;
	
	public ItemDto toDto() {
		ItemDto itemDto = ItemDto.builder()
				.id(this.id)
				.title(this.title)
				.content(this.content)
				.category(this.category)
				.viewCnt(this.viewCnt)
				.wr_date(this.wr_date)
				.re_date(this.re_date)
				.del_date(this.del_date)
				.build();
		
		return itemDto;
	}
}
