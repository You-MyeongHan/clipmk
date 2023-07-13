package com.bayclip.user.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfo {
	private String uid;
	private String nick;
	private String email;
	private Boolean emailReceive;
	private Long boardCnt;
	private Long commentCnt;
}
