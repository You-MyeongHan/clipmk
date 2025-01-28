package com.clipmk;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {
    
    @GetMapping("/register")
    public String registerForm() {
        return "view/member/register";
    }
} 