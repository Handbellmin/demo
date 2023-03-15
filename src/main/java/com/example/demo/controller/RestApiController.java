package com.example.demo.controller;

import com.example.demo.auth.PrincipalDetails;
import com.example.demo.auth.PrincipalDetailsService;
import com.example.demo.auth.jwt.JwtTokenService;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import com.p6spy.engine.logging.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManagerBuilder authenticationManager;

    private final PrincipalDetailsService principalDetailsService;
    private final JwtTokenService jwtTokenService;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user, HttpServletResponse response){
        try {

            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(user.getUsername());
            String accessToken = jwtTokenService.createAccessToken(user.getUsername());
            String refreshToken = jwtTokenService.createRefreshToken(user.getUsername());
            Cookie AccessCookie = new Cookie("accessToken", accessToken);
            Cookie RefreshCookie = new Cookie("accessToken", refreshToken);
            AccessCookie.setHttpOnly(true);
            RefreshCookie.setHttpOnly(true);
            RefreshCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));
            AccessCookie.setMaxAge((int) TimeUnit.MINUTES.toSeconds(30));
            response.addCookie(AccessCookie);
            response.addCookie(RefreshCookie);

            return ResponseEntity.ok().build();
        } catch(AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> testwrite(Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName());
    }
    //user 권한 접근 가능
    @GetMapping("/api/v1/user")
    public String user(Authentication authentication, HttpServletRequest request) {

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
