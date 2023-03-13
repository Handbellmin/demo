package com.example.demo.controller;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입 완료";
    }

    @PostMapping
    public ResponseEntity<String> testwrite(Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName());
    }
    //user 권한 접근 가능
    @GetMapping("/api/v1/user")
    public String user(Authentication authentication) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        return "user";
    }

    //manager, admin 권한 접근 가능
    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }


    //admin 권한 접근 가능
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}
