package com.bayclip.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Value("${server.front-server}")
	private String frontServer;
	
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();	
    }
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	// 메세지 받을 때 URL
        registry.enableSimpleBroker("/sub");
        // 메세지 보낼 때 URL
        registry.setApplicationDestinationPrefixes("/pub");
    }
}