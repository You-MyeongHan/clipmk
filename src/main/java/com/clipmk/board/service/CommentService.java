package com.clipmk.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clipmk.board.dto.CommentDto;
import com.clipmk.board.dto.EditCommentReqDto;
import com.clipmk.board.dto.PostDto;
import com.clipmk.board.dto.RecommendCntResDto;
import com.clipmk.board.entity.Comment;
import com.clipmk.board.entity.CommentReaction;
import com.clipmk.board.entity.Post;
import com.clipmk.board.repository.BoardRepository;
import com.clipmk.board.repository.CommentRepository;
import com.clipmk.user.entity.User;
import com.clipmk.user.repository.UserRepository;
import com.global.error.errorCode.BoardErrorCode;
import com.global.error.errorCode.UserErrorCode;
import com.global.error.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    @Value("${point.recommend-comment}")
	private int RCP;	//Recommend Comment Point
	@Value("${point.create-comment}")
	private int CCP;	//Create Comment Point
	
	public List<CommentDto> getComments(long postId) {
		List<Comment> comments = commentRepository.findByPostIdAndParentIsNull(postId);
		List<CommentDto> commentDtos = new ArrayList<>();
		
		for (Comment comment : comments) {
            CommentDto commentDto = CommentDto.from(comment);
            List<CommentDto> replyDtos = comment.getReplies();
            commentDto.setReplies(replyDtos);
            commentDtos.add(commentDto);
        }
		return commentDtos;
	}
	
    @Transactional
    public void createComment(Long postId, User user, String content) {
    	
    	Post post= boardRepository.findById(postId).orElseThrow(
				()-> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
    	
		var comment= Comment.builder()
					.content(content)
					.user(user)
					.post(post)
					.build();
		commentRepository.save(comment);

    }
    
    @Transactional
    public RecommendCntResDto getRecommendCnt(Long postId, User user) {
    	Post post = boardRepository.findById(postId).orElseThrow(
				()-> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
    	
    	RecommendCntResDto recommendCntResponseDto = RecommendCntResDto
														.builder()
														.recommend_cnt((int) post.getRecommendCount())
														.decommend_cnt((int) post.getDecommendCount())
														.build();
    	
    	if(user != null) {
    		recommendCntResponseDto.setRecommend_state(post.getRecommendState(user));
    	}
    	
    	return recommendCntResponseDto;
    }
    
    @Transactional
    public void createReply(Long postId, Long parentId, User user, String content) {
    	
    	Post post= boardRepository.findById(postId).orElseThrow(
				()-> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
    	
    	Comment parentComment = commentRepository.findById(parentId).orElseThrow(
    			() -> new RestApiException(BoardErrorCode.COMMENT_NOT_FOUND));
    	
    	Integer point = user.getPoint();
    	
		Comment comment= Comment.builder()
					.content(content)
					.user(user)
					.post(post)
					.parent(parentComment)
					.build();
		point+=CCP;
		userRepository.save(user);
		commentRepository.save(comment);
    }
    
    public List<Comment> findByPostId(Long postId){
    	return commentRepository.findByPostId(postId);
    }
    
    public Page<Comment> findTop10ByUser_IdOrderByComment_IdDesc(Integer userId, Pageable pageable){
		return commentRepository.findTop10ByUser_IdOrderByIdDesc(userId, pageable);
	}
    
    @Transactional
    public void deleteComment(Long commentId, User user) {
    	Comment comment = commentRepository.findById(commentId).orElseThrow(
    			() -> new RestApiException(BoardErrorCode.COMMENT_NOT_FOUND));
    	
    	if(user == null) {
			throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
		}
    	
    	Integer point = user.getPoint();
    	
		comment.setDelYn("Y");  // 삭제 여부를 Y로 설정
		point -= CCP;
		userRepository.save(user);
    	commentRepository.save(comment);
    }
    
    @Transactional
	public void edit(Long commentId, EditCommentReqDto request, User user) {
		
    	Comment comment=commentRepository.findById(commentId).orElseThrow(
    			() -> new RestApiException(BoardErrorCode.COMMENT_NOT_FOUND));
    	
		if(user==null) {
			throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
		}

        comment.setContent(request.getContent());
        commentRepository.save(comment);
	}
    
    @Transactional
	public void recommendComment(Long commentId, User user, int value) {
		if (user == null) {
			throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
		}

		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new RestApiException(BoardErrorCode.COMMENT_NOT_FOUND));
		
		User commentUser = comment.getUser();
		Integer point = commentUser.getPoint();

		CommentReaction.ReactionType type = value == 1 ? 
			CommentReaction.ReactionType.UP : CommentReaction.ReactionType.DOWN;

		// 현재 반응 상태 확인
		int currentState = comment.getRecommendState(user);
		
		// 반응 토글
		comment.toggleReaction(user, type);
		
		// 포인트 계산
		if (currentState == 0) {
			// 새로운 반응 추가
			point += value == 1 ? RCP : -RCP;
		} else if (currentState == value) {
			// 같은 반응 취소
			point += value == 1 ? -RCP : RCP;
		} else {
			// 반대 반응으로 변경
			point += value == 1 ? 2 * RCP : -2 * RCP;
		}

		commentUser.setPoint(point);
		userRepository.save(commentUser);
		commentRepository.save(comment);
	}
    
    @Transactional
	public RecommendCntResDto getCommentRecommendCnt(Long commentId, User user) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new RestApiException(BoardErrorCode.COMMENT_NOT_FOUND));
		
		RecommendCntResDto recommendCntResponseDto = RecommendCntResDto.builder()
				.recommend_cnt((int) comment.getRecommendCount())
				.decommend_cnt((int) comment.getDecommendCount())
				.recommend_state(comment.getRecommendState(user))
				.build();
		
		return recommendCntResponseDto;
	}
    
    public Page<Comment> findByUserId(int userId, Pageable pageable){
		Page<Comment> comments = commentRepository.findByUserId(userId, pageable);
		return comments;
	}
    
}