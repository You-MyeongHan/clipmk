package com.bayclip.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.bayclip.board.dto.CommentDto;
import com.bayclip.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@Builder.Default
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> replies= new ArrayList<>();
	
	@Builder.Default
	@ElementCollection
	@CollectionTable(
		name="comment_recommendations",
		joinColumns=@JoinColumn(name="comment_id")
	)
	@Column(name="user_id")
	private Set<Integer> recommendations=new HashSet<>();
	
	@Builder.Default
	@ElementCollection
	@CollectionTable(
		name="comment_decommendations",
		joinColumns=@JoinColumn(name="comment_id")
	)
	private Set<Integer> decommendations=new HashSet<>();
	
	public List<CommentDto> getReplies(){
		List<CommentDto> replyDtos = new ArrayList<>();
        for (Comment reply : this.replies) {
        	CommentDto replyDto = CommentDto.from(reply);
            replyDto.setParent_nick(this.getUser().getNick());
            replyDtos.add(replyDto);
            replyDtos.addAll(reply.getReplies());
        }
        return replyDtos;
	}
	
}