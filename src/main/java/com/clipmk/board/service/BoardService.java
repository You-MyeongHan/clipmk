package com.clipmk.board.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.AmazonS3Client;
import com.clipmk.board.dto.CommentDto;
import com.clipmk.board.dto.EditPostReqDto;
import com.clipmk.board.dto.PostDto;
import com.clipmk.board.dto.PostReqDto;
import com.clipmk.board.dto.PostsResDto;
import com.clipmk.board.entity.Comment;
import com.clipmk.board.entity.Post;
import com.clipmk.board.entity.PostReaction;
import com.clipmk.board.repository.BoardRepository;
import com.clipmk.board.repository.CommentRepository;
import com.clipmk.user.entity.User;
import com.clipmk.user.repository.UserRepository;
import com.global.error.errorCode.BoardErrorCode;
import com.global.error.errorCode.UserErrorCode;
import com.global.error.exception.RestApiException;
import com.infra.meta.dto.PostIdDto;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	// private final AmazonS3Client amazonS3Client;
	@Value("${point.recommend-post}")
	private int RPP;	//Recommend Post Point
	// @Value("${point.create-post}")
	// private int RPP;	//Recommend Post Point 
	// @Value("${cloud.aws.s3.bucket.url}")
    // private String defaultUrl;
	// @Value("${cloud.aws.s3.bucket.name}")
    // public String bucket;

	@Transactional
	public Page<PostsResDto> findAll(Pageable pageable) {
		return boardRepository.findPosts(pageable);
	}
	
	@Transactional
	// public PostDto getPostById(Long postId, User user, HttpSession session) {
	public PostDto getPostById(Long postId) {
		
		Post post=boardRepository.findById(postId).orElseThrow(
				()-> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
		// Set<Long> viewedPostIds = (Set<Long>) session.getAttribute("viewedPostIds");
		
		// if (viewedPostIds == null) {
        //     viewedPostIds = new HashSet<>();
        // }
		
		// if (!viewedPostIds.contains(postId)) {
        // 	post.setViewCnt(post.getViewCnt()+1);
        //     boardRepository.save(post);
        //     viewedPostIds.add(postId);
        // }
		// session.setAttribute("viewedPostIds", viewedPostIds);
		

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
		// if(user!=null) {
		// 	if(post.getRecommendations().contains(user.getId()))
		// 		postDto.setRecommend_state(1);
		// 	else if(post.getDecommendations().contains(user.getId()))
		// 		postDto.setRecommend_state(-1);
		// 	else
		// 		postDto.setRecommend_state(0);
		// }
		
		return postDto;
        
	}
	
//	public Long getBoardCnt(Integer userId) {
//		
//		return boardRepository.countByUser_Id(userId);
//	}
	
	@Transactional
	public PostDto edit(Long postId, EditPostReqDto request, User user) {
		
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
        if (request.getSubCategory() != null) {
            post.setSubCategory(request.getSubCategory());
        }
        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }
        if (request.getThumbnail() != null) {
            post.setThumbnail(request.getThumbnail());
        }
        
		Post newPost = boardRepository.save(post);
		
		return newPost.toDto();
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
	
	public Page<Post> findByViewCntGreaterThan(Integer viewCount, Pageable pageable){
		return boardRepository.findByViewCntGreaterThan(viewCount, pageable);
	}
	
	public Page<Post> findTop10ByUser_IdOrderByIdDesc(Integer userId, Pageable pageable){
		return boardRepository.findTop10ByUserIdOrderByPostIdDesc(userId, pageable);
	}
	
	@Transactional
	public boolean register(PostReqDto request, Integer userId) {
		
		User user=userRepository.findById(userId).orElseThrow(
				()-> new RestApiException(UserErrorCode.USER_NOT_FOUND));
		
		String htmlContent=parseContentAndMoveImages(request.getContent());
		Document doc = Jsoup.parse(htmlContent);
		String thumbnail = null;
		Elements imgTags = doc.select("img");
		if (imgTags != null) {
            String srcAttribute = imgTags.attr("src");
            if (srcAttribute.contains("https://clipmarkets3.s3.ap-northeast-2.amazonaws.com/posts/images/main")) {
                thumbnail = srcAttribute;
            }
        }
		
		Integer point = user.getPoint();
		Post post=Post.builder()
				.title(request.getTitle())
				.category(request.getCategory())
				.subCategory(request.getSubCategory())
				.content(htmlContent)
				.user(user)
				.thumbnail(thumbnail)
				.build();
		point+=RPP;
		
		user.setPoint(point);
		userRepository.save(user);
		boardRepository.save(post);
		
		return true;
	}
	
	private String parseContentAndMoveImages(String content) {
		Document doc = Jsoup.parse(content);
		Elements images = doc.getElementsByTag("img");
		
		if (images.size() > 0) {
	        for (Element image : images) {
	            String source = image.attr("src");

	            if (!source.contains("/temp/")) {
	                continue;
	            }

	            source = source.replace(defaultUrl, "");
	            String newSource = "posts/images/main/"+LocalDate.now().toString() + "/" + source.split("/")[4];

	            content = content.replace(source, newSource);

	            update(source, newSource);

	        }
	    }
		
		return content;
	}
	
	public void update(String oldSource, String newSource) {
	    try {
	        oldSource = URLDecoder.decode(oldSource, "UTF-8");
	        newSource = URLDecoder.decode(newSource, "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }

	    // moveS3(oldSource, newSource);
	    // deleteS3(oldSource);
	}
	
	// private void moveS3(String oldSource, String newSource) {
	// 	amazonS3Client.copyObject(bucket, oldSource, bucket, newSource);
	// }

	// private void deleteS3(String source) {
	// 	amazonS3Client.deleteObject(bucket, source);
	// }
	
	@Transactional
	public void updateView_cnt(Long postId) {
		Post post=boardRepository.findById(postId).orElseThrow(()->
			new IllegalStateException("게시물이 존재하지 않습니다."));
		
		post.updateViewCnt(post.getViewCnt());
	}
	
	@Transactional
	public void recommend(Long postId, User user, int value) {
		if (user == null) {
			throw new RestApiException(UserErrorCode.USER_NOT_FOUND);
		}

		Post post = boardRepository.findById(postId)
				.orElseThrow(() -> new RestApiException(BoardErrorCode.POST_NOT_FOUND));
		
		User postUser = post.getUser();
		Integer point = postUser.getPoint();

		PostReaction.ReactionType type = value == 1 ? 
			PostReaction.ReactionType.UP : PostReaction.ReactionType.DOWN;

		// 현재 반응 상태 확인
		int currentState = post.getRecommendState(user);
		
		// 반응 토글
		post.toggleReaction(user, type);
		
		// 포인트 계산
		if (currentState == 0) {
			// 새로운 반응 추가
			point += value == 1 ? RPP : -RPP;
		} else if (currentState == value) {
			// 같은 반응 취소
			point += value == 1 ? -RPP : RPP;
		} else {
			// 반대 반응으로 변경
			point += value == 1 ? 2 * RPP : -2 * RPP;
		}

		postUser.setPoint(point);
		userRepository.save(postUser);
		boardRepository.save(post);
	}
	
	public Page<Post> findByUserId(int userId, Pageable pageable){
		Page<Post> posts = boardRepository.findByUserId(userId, pageable);
		return posts;
	}
	
	public List<PostIdDto> getPostsIds(){
		List<PostIdDto> postsIds= boardRepository.findAllPostIds();
		return postsIds;
	}
}