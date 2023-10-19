package com.bayclip.barter.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.bayclip.barter.dto.ItemResDto;
import com.bayclip.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.annotation.Nullable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name="item")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
	@Nullable
	private String region;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	@JsonIgnore
    private User user;
	
	@ElementCollection
	@CollectionTable(
		name="item_interested",
		joinColumns=@JoinColumn(name="item_id")
	)
	@Column(name="user_id")
	private Set<Integer> interested=new HashSet<>();
	
	public ItemResDto toDto() {
		ItemResDto itemDto = ItemResDto.builder()
				.id(this.id)
				.title(this.title)
				.content(this.content)
				.category(this.category)
				.viewCnt(this.viewCnt)
				.wr_date(this.wr_date)
				.re_date(this.re_date)
				.del_date(this.del_date)
				.user_id(this.user.getId())
				.user_nick(this.user.getNick())
				.build();
		
		return itemDto;
	}
}
