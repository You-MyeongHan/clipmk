package com.clipmk.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.clipmk.board.entity.Comment;
import com.clipmk.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
	private Long id;
	private String content;
	private LocalDateTime  wr_date;
	private LocalDateTime  del_date;
	private Integer user_id;
	private String user_nick;
	private String parent_nick;
	private int recommend_cnt;
	private int decommend_cnt;
	private int recommend_state;
	@Builder.Default
	private List<CommentDto> replies = new ArrayList<>();
	
	public static CommentDto from(Comment comment) {
        CommentDto dto = CommentDto.builder()
        		.id(comment.getCommentId())
        		.content(comment.getContent())
        		.wr_date(comment.getFrsRgDtm())
        		.del_date(comment.getDelYn() != null ? LocalDateTime.now() : null)
        		.user_id(comment.getUser().getId())
        		.user_nick(comment.getUser().getNick())
        		.recommend_cnt((int) comment.getRecommendCount())
        		.decommend_cnt((int) comment.getDecommendCount())
        		.build();

        return dto;
    }
	
	public static CommentDto from(Comment comment, User currentUser) {
        CommentDto dto = from(comment);
        dto.setRecommend_state(comment.getRecommendState(currentUser));
        return dto;
    }
	
	public String getContent() {
        if (del_date != null) {
            return "(삭제됨)";
        }
        return content;
    }
}
