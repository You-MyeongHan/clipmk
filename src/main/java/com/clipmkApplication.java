package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
	"com.clipmk", 
	"com.global",
	"com.infra" 
})
public class clipmkApplication {
	public static void main(String[] args) {
		SpringApplication.run(clipmkApplication.class, args);
	}
}