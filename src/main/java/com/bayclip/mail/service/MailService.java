package com.bayclip.mail.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bayclip.board.repository.BoardRepository;
import com.bayclip.board.repository.CommentRepository;
import com.bayclip.mail.entity.Mail;
import com.bayclip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private JavaMailSender mailSender;
	private static final String FROM_USER_NAME="${spring.mail.username}";
	
	public void mailSend(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setFrom(FROM_USER_NAME);
        message.setSubject(mail.getTitle());
        message.setText(mail.getMessage());

        mailSender.send(message);
    }
}
