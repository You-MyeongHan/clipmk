package com.bayclip.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.bayclip.board.dto.PostDto;
import com.bayclip.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
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
@Table(name="post")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String category;
	@Column(length = 3000)
	private String content;
	
	@CreationTimestamp
	private LocalDateTime  wr_date;
	private LocalDateTime  re_date;
	private LocalDateTime  del_date;
	
	@ColumnDefault("0")
	@Column(name="view_cnt")
	private Integer viewCnt;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	@JsonIgnore
    private User user;
	private String thumbnail;
	
	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
	
	@Builder.Default
	@ElementCollection
	@CollectionTable(
		name="post_recommendations",
		joinColumns=@JoinColumn(name="post_id")
	)
	@Column(name="user_id")
	private Set<Integer> recommendations=new HashSet<>();
	
	@Builder.Default
	@ElementCollection
	@CollectionTable(
		name="post_decommendations",
		joinColumns=@JoinColumn(name="post_id")
	)
	@Column(name="user_id")
	private Set<Integer> decommendations=new HashSet<>();
	
	@Transactional
	public void updateViewCnt(Integer visit) {
		this.viewCnt=visit;
	}
	
	public PostDto toDto() {
		PostDto postDto = PostDto.builder()
				.id(this.id)
				.title(this.title)
				.category(this.category)
				.content(this.content)
				.thumbnail(this.thumbnail)
				.wr_date(this.wr_date)
				.re_date(this.re_date)
				.del_date(this.del_date)
				.viewCnt(this.viewCnt)
				.user_nick(this.user.getNick())
				.user_id(this.user.getId())
				.recommend_cnt(this.getRecommendations().size())
				.decommend_cnt(this.getDecommendations().size())
				.build();
		return postDto;
	}
}
