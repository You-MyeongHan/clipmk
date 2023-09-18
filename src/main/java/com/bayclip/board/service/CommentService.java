package com.bayclip.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.board.dto.EditCommentRequestDto;
import com.bayclip.board.entity.Comment;
import com.bayclip.board.entity.Post;
import com.bayclip.board.repository.BoardRepository;
import com.bayclip.board.repository.CommentRepository;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;

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
	
    @Transactional
    public boolean createComment(Long postId, User user, String content) {
    	Post post = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID"));
    	if(boardRepository.existsById(postId)) {
    		var comment= Comment.builder()
    					.content(content)
    					.user(user)
    					.post(post)
    					.build();
    		commentRepository.save(comment);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    @Transactional
    public boolean createReply(Long postId, Long parentId, User user, String content) {
    	
    	Post post = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID"));
    	Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID"));
    	Integer point = user.getPoint();
    	
    	if(post!=null && parentComment!=null) {
    		Comment comment= Comment.builder()
						.content(content)
						.user(user)
						.post(post)
						.parent(parentComment)
						.build();
    		point+=CCP;
    		userRepository.save(user);
    		commentRepository.save(comment);
			return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public List<Comment> findByPostId(Long postId){
    	return commentRepository.findByPostId(postId);
    }
    
    public Page<Comment> findTop10ByUser_IdOrderByComment_IdDesc(Integer userId, Pageable pageable){
		return commentRepository.findTop10ByUser_IdOrderByIdDesc(userId, pageable);
	}
    
    @Transactional
    public boolean deleteComment(Long commentId, User user) {
    	
    	Comment comment=commentRepository.findById(commentId).orElseThrow(null);
    	Integer point = user.getPoint();
    	
    	if(user!=null && comment!=null) {
    		comment.setDel_date(LocalDateTime.now());
    		point-=CCP;
    		userRepository.save(user);
        	commentRepository.save(comment);
        	return true;
    	}
    	
    	return false;
    }
    
    @Transactional
	public boolean edit(Long commentId, EditCommentRequestDto request, User user) {
		
		Comment comment= commentRepository.findById(commentId).orElse(null);
		
		if(user!=null && comment!=null) {
			
            if (request.getContent() != null) {
                comment.setContent(request.getContent());
                commentRepository.save(comment);
            }
			
			return true;

		}
		
		return false;
	}
    
    @Transactional
	public Boolean recommend(Long commentId, User user, int value) {
		
		Comment comment= commentRepository.findById(commentId).orElse(null);
		if(comment!=null) {
			if(user!=null) {
				
				Set<Integer> recommendations = comment.getRecommendations();
		        Set<Integer> decommendations = comment.getDecommendations();
		        Integer point = user.getPoint();
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
		        userRepository.save(user);
		        return true;
			}
		}
		
		return false;
	}
    
}