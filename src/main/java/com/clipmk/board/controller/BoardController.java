package com.clipmk.board.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clipmk.board.dto.PostReqDto;
import com.clipmk.board.service.BoardService;
import com.clipmk.user.entity.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //게시물 등록
    @PostMapping("/post")
    public ResponseEntity<Void> register(
            @RequestBody PostReqDto request,
            @AuthenticationPrincipal User user
    ){
        
        boardService.register(request, user.getId());
        
        return ResponseEntity.ok().build();
    }
} 
