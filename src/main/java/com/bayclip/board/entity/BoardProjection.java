package com.bayclip.board.entity;

public interface BoardProjection {
    Long getId();
    String getTitle();
    String getCategory();
    String getContent();
    Long getUser_Id();
    String getUser_Nick();
}