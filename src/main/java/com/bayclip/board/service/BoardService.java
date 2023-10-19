package com.bayclip.board.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.board.dto.CommentDto;
import com.bayclip.board.dto.EditPostReqDto;
import com.bayclip.board.dto.PostDto;
import com.bayclip.board.dto.PostReqDto;
import com.bayclip.board.entity.Comment;
import com.bayclip.board.entity.Post;
import com.bayclip.board.repository.BoardRepository;
import com.bayclip.board.repository.CommentRepository;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;
import com.global.error.errorCode.BarterErrorCode;
import com.global.error.errorCode.BoardErrorCode;
import com.global.error.errorCode.UserErrorCode;
import com.global.error.exception.RestApiException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	@Value("${point.recommend-post}")
	private int RPP;	//Recommend Post Point
	@Value("${point.create-post}")
	private int CPP;	//Recommend Post Point 
	
	@Transactional
	public PostDto getPostById(Long postId, User user, HttpSession session) {
		
		Post post=boardRepository.findById(postId).orElseThrow(
				()-> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
		Set<Long> viewedPostIds = (Set<Long>) session.getAttribute("viewedPostIds");
		
		if (viewedPostIds == null) {
            viewedPostIds = new HashSet<>();
        }
		
		if (!viewedPostIds.contains(postId)) {
        	post.setViewCnt(post.getViewCnt()+1);
            boardRepository.save(post);
            viewedPostIds.add(postId);
        }
		session.setAttribute("viewedPostIds", viewedPostIds);
		

		PostDto postDto = post.toDto();
		
		List<Comment> comments = commentRepository.findByPostIdAndParentIsNull(postId);
		List<CommentDto> commentDtos = new ArrayList<>();
		
		for (Comment comment : comments) {
            CommentDto commentDto = CommentDto.from(comment);
            List<CommentDto> replyDtos = comment.getReplies();
            commentDto.setReplies(replyDtos);
            commentDtos.add(commentDto);
        }

		postDto.setComments(commentDtos);
		if(user!=null) {
			if(post.getRecommendations().contains(user.getId()))
				postDto.setRecommend_state(1);
			else if(post.getDecommendations().contains(user.getId()))
				postDto.setRecommend_state(-1);
			else
				postDto.setRecommend_state(0);
		}
		
		return postDto;
        
	}
	
//	public Long getBoardCnt(Integer userId) {
//		
//		return boardRepository.countByUser_Id(userId);
//	}
	
	@Transactional
	public Boolean edit(Long postId, EditPostReqDto request, User user) {
		
		Post post= boardRepository.findById(postId).orElseThrow(
				()-> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
		
		if (!post.getUser().getId().equals(user.getId())) {
	        throw new RestApiException(BoardErrorCode.POST_ACCESS_DENIED);
	    }
		
		if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
        }
        if (request.getCategory() != null) {
            post.setCategory(request.getCategory());
        }
        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }
        if (request.getThumbnail() != null) {
            post.setThumbnail(request.getThumbnail());
        }
        
		boardRepository.save(post);
		
		return true;
	}
	
	@Transactional
	public void delete(Long postId, User user) {
		
		Post post= boardRepository.findById(postId).orElseThrow(
				()-> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
		
		if (!post.getUser().getId().equals(user.getId())) {
	        throw new RestApiException(BoardErrorCode.POST_ACCESS_DENIED);
	    }
		
		boardRepository.delete(post);
	}
	
	public Page<Post> findAll(Pageable pageable, String category) {
		
		Specification<Post> spec = null;
		if(category !=null && !category.isEmpty()) {
			spec = (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("category"), category);
		}
		
		if (spec != null) {
            return boardRepository.findAll(spec, pageable);
        } else {
            return boardRepository.findAll(pageable);
        }
	}
	
	public Page<Post> findByViewCntGreaterThan(Integer viewCount, Pageable pageable){
		return boardRepository.findByViewCntGreaterThan(viewCount, pageable);
	}
	
	public Page<Post> findByTitleContaining(Pageable pageable, String searchTerm){
		return boardRepository.findByTitleContaining(pageable, searchTerm);
	}
	
	public Page<Post> findTop10ByUser_IdOrderByIdDesc(Integer userId, Pageable pageable){
		return boardRepository.findTop10ByUser_IdOrderByIdDesc(userId, pageable);
	}
	
	@Transactional
	public boolean register(PostReqDto request, Integer userId) {
		
		User user=userRepository.findById(userId).orElseThrow(
				()-> new RestApiException(UserErrorCode.USER_NOT_FOUND));
		
		Integer point = user.getPoint();
		Post post=Post.builder()
				.title(request.getTitle())
				.category(request.getCategory())
				.content(request.getContent())
				.user(user)
				.thumbnail(request.getThumbnail())
				.build();
		point+=CPP;
		
		user.setPoint(point);
		userRepository.save(user);
		boardRepository.save(post);
		
		return true;
	}
	
	@Transactional
	public void updateView_cnt(Long postId) {
		Post post=boardRepository.findById(postId).orElseThrow(()->
			new IllegalStateException("게시물이 존재하지 않습니다."));
		
		post.updateViewCnt(post.getViewCnt());
	}
	
	@Transactional
	public void recommend(Long postId, User user, int value) {
		
		Post post= boardRepository.findById(postId).orElseThrow(
				()-> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
		if(user==null) {
			throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
		}
		User postUser=post.getUser();
			
		Set<Integer> recommendations = post.getRecommendations();
        Set<Integer> decommendations = post.getDecommendations();
        Integer point = postUser.getPoint();
        if (value == 1) {
            // 추천 버튼 클릭
            if(decommendations.remove(user.getId())) {
            	point+=RPP;
            }

            if (recommendations.contains(user.getId())) {
                // 이미 추천한 경우, 추천 취소
                recommendations.remove(user.getId());
                point-=RPP;
            } else {
            	recommendations.add(user.getId());
            	point+=RPP;
            }
        } else if (value == -1) {
            // 비추천 버튼 클릭
            if(recommendations.remove(user.getId())) {
            	point-=RPP;
            }

            if (decommendations.contains(user.getId())) {
                // 이미 비추천한 경우, 비추천 취소
                decommendations.remove(user.getId());
                point+=RPP;
            }
            else{
                // 비추천 추가
            	decommendations.add(user.getId());
            	point-=RPP;
            }
        }
        boardRepository.save(post);
        postUser.setPoint(point);
        userRepository.save(postUser);
	}
	
	public Page<Post> findByUserId(int userId, Pageable pageable){
		Page<Post> posts = boardRepository.findByUserId(userId, pageable);
		return posts;
	}
}