package com.bayclip.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;

import com.bayclip.board.dto.CommentDto;
import com.bayclip.board.dto.ReplyCommentDto;
import com.bayclip.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "comment")
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String content;
	
	@CreationTimestamp
	private LocalDateTime  wr_date;
	private LocalDateTime  del_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	@JsonIgnore
    private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
	@JsonIgnore
    private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
	
	@ElementCollection
	@CollectionTable(
		name="comment_recommendations",
		joinColumns=@JoinColumn(name="comment_id")
	)
	@Column(name="user_id")
	private Set<Integer> recommendations=new HashSet<>();
	
	@ElementCollection
	@CollectionTable(
		name="comment_decommendations",
		joinColumns=@JoinColumn(name="comment_id")
	)
	private Set<Integer> decommendations=new HashSet<>();
	
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> replies= new ArrayList<>();
	
	public CommentDto toDto() {
		CommentDto dto = CommentDto.builder()
				.id(this.id)
				.content(this.content)
				.wr_date(this.wr_date)
				.del_date(this.del_date)
				.user_id(this.user.getId())
				.user_nick(this.user.getNick())
				.recommend_cnt(this.recommendations.size())
				.decommend_cnt(this.decommendations.size())
				.build();

        List<CommentDto> replyDtos = new ArrayList<>();
        for (Comment reply : replies) {
            replyDtos.add(reply.toDto());
        }
        dto.setReplies(replyDtos);

        return dto;
	}
	
}