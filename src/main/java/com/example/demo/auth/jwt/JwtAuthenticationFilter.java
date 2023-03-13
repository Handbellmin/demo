package com.example.demo.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.auth.PrincipalDetails;
import com.example.demo.model.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.stream.Collectors;

//스프링 시큐리티에 있는 필터 (UsernamePasswordAuthenticationFilter)
// /login 요청을 할때, username, password 전송하면 이 필터 동작
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 sucessfualAuthentication 함수가 실행
    // JWT 토큰을 만들어서 request요청한 사용자에게 JWT토른을 response해줌
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAuthentication 실행");
        PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                        .withExpiresAt(new Date(System.currentTimeMillis()+(60000*5)))
                                .withClaim("id",principalDetails.getUser().getUsername())
                                        .withClaim("username",principalDetails.getUser().getUsername())
                                                .sign(Algorithm.HMAC512("demoApplicationv3"));
        //로그인된 사용자에게 전해주는 응답헤더
        response.addHeader("Authorization","Bearer "+jwtToken);
        setHttpOnlyCookie(response,"Authorization","Bearer",100);
        String targetUrl = determineTargetUrl(request,authResult);

        //redirectStrategy.sendRedirect(request,response,targetUrl);
    }
    public void setHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

        private String determineTargetUrl(HttpServletRequest request, Authentication authResult) {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        for (GrantedAuthority authority : principalDetails.getAuthorities()) {
            System.out.println(authority.getAuthority());
            if (authority.getAuthority().contains(("ROLE_USER"))) {
                return "hello";
            } else if (authority.getAuthority().contains(("ROLE_ADMIN"))) {
                return "/hello";
            }
        }
            return null;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //1. username, password 받아서
        try {
            ObjectMapper om = new ObjectMapper();
            Users user = om.readValue(request.getInputStream(), Users.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //principalDetails Service loadUserByname() 함수 실행.
            Authentication authentication = authenticationManager.authenticate(token);
            
            //authentication 객체가 session 영역에 저장 => 로그인 완료!
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료 :"+ principalDetails.getUser().getUsername());

            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
}
