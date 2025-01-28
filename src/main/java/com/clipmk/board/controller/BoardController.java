package com.clipmk.board.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id) {
        // 추후 게시글 데이터를 모델에 추가하는 로직이 들어갈 자리
        return "view/board/post";  // templates/view/board/post.html을 찾아 렌더링
    }
    
    @GetMapping("/free")
    public String freeBoard() {
        return "view/board/list";  // 자유게시판 목록 페이지
    }
} 
