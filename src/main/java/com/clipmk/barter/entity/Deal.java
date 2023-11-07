package com.clipmk.barter.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.clipmk.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name="deal")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Deal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_user_id")
	private User fromUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id")
	private User toUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_item_id")
    private Item fromItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_item_id")
    private Item toItem;
	
	@Builder.Default()
	private int status=0;
	
	@CreationTimestamp
	private LocalDateTime  wr_date;
	private LocalDateTime  del_date;
	private LocalDateTime  cmp_date;
	
}
