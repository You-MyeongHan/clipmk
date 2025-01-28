package com.clipmk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
	"com.clipmk", 
	"com.global",
	"com.infra"
})
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

	// @RestController
	// class Test{
	// 	@GetMapping("/test")
	// 	public String test() {
	// 		return "Test입니다.";
	// 	}
	// }
}
