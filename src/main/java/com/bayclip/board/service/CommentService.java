package com.bayclip.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.board.dto.CommentDto;
import com.bayclip.board.dto.EditCommentReqDto;
import com.bayclip.board.dto.PostDto;
import com.bayclip.board.dto.RecommendCntResDto;
import com.bayclip.board.entity.Comment;
import com.bayclip.board.entity.Post;
import com.bayclip.board.repository.BoardRepository;
import com.bayclip.board.repository.CommentRepository;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;
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
	private int RCP;	//Recommend Post Point
	@Value("${point.create-comment}")
	private int CCP;	//Recommend Post Point
	
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
    	
    	Post post= boardRepository.findById(postId).orElseThrow(
				()-> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
    	
    	PostDto postDto=post.toDto();
    	
    	RecommendCntResDto recommendCntResponseDto = RecommendCntResDto
														.builder()
														.recommend_cnt(postDto.getRecommend_cnt())
														.decommend_cnt(postDto.getDecommend_cnt())
														.build();
    	
    	if(user!=null) {
			if(post.getRecommendations().contains(user.getId()))
				recommendCntResponseDto.setRecommend_state(1);
			else if(post.getDecommendations().contains(user.getId()))
				recommendCntResponseDto.setRecommend_state(-1);
			else
				recommendCntResponseDto.setRecommend_state(0);
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
    	
    	Comment comment=commentRepository.findById(commentId).orElseThrow(
    			() -> new RestApiException(BoardErrorCode.COMMENT_NOT_FOUND));
    	
    	if(user==null) {
			throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
		}
    	
    	Integer point = user.getPoint();
    	
		comment.setDel_date(LocalDateTime.now());
		point-=CCP;
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
	public void recommend(Long commentId, User user, int value) {
		
    	Comment comment=commentRepository.findById(commentId).orElseThrow(
    			() -> new RestApiException(BoardErrorCode.COMMENT_NOT_FOUND));
    	
    	if(user==null) {
			throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
		}
    	
		User postUser=comment.getUser();
				
		Set<Integer> recommendations = comment.getRecommendations();
        Set<Integer> decommendations = comment.getDecommendations();
        Integer point = postUser.getPoint();
        if (value == 1) {
            // 추천 버튼 클릭
            if(decommendations.remove(user.getId())) {
            	point+=RCP;
            }

            if (recommendations.contains(user.getId())) {
                // 이미 추천한 경우, 추천 취소
                recommendations.remove(user.getId());
                point-=RCP;
            } else {
            	recommendations.add(user.getId());
            	point+=RCP;
            }
        } else if (value == -1) {
            // 비추천 버튼 클릭
            if(recommendations.remove(user.getId())) {
            	point-=RCP;
            }

            if (decommendations.contains(user.getId())) {
                // 이미 비추천한 경우, 비추천 취소
                decommendations.remove(user.getId());
                point+=RCP;
            }
            else{
                // 비추천 추가
            	decommendations.add(user.getId());
            	point-=RCP;
            }
        }
        commentRepository.save(comment);
        postUser.setPoint(point);
        userRepository.save(postUser);

	}
    
}