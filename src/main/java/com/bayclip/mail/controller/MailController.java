package com.bayclip.mail.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bayclip.mail.entity.Mail;
import com.bayclip.mail.service.MailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
	private final MailService mailService;
	
	@PostMapping("/authenticate")
	public void authenticateMail(Mail mailDto) {
		mailService.mailSend(mailDto);
	}
}
