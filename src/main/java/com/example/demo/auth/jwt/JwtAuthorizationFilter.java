package com.example.demo.auth.jwt;

// 시큐리티가 filter가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음.
// 만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 안 타도록.

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.auth.PrincipalDetails;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    private JwtTokenService jwtTokenService;

    @Value("${jwt.secret}")
    private String secretKey;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager , UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    //인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨.");
        String jwtHaeader_access = request.getHeader("AccessToken");
        String jwtHaeader_refresh = request.getHeader("refreshToken");
        //String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // header 가 있는지 확인
        /*if(jwtHeader == null || !jwtHeader.startsWith("Bearer ")){
            chain.doFilter(request,response);
            return;
        }*/
        //access Token 이  null 이면 refreshToken을 확인한다.
        if(jwtHaeader_access == null) {
            System.out.println("jwtHeader_access is null.");
        }else {
            System.out.println("jwtHeader_access is not null.");
            // JWT 토큰을 검증해서 정상적인 사용자인지 확인
            //String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        /*String username =
                JWT.require(Algorithm.HMAC512("demoApplication_v3")).build().verify(jwtToken).getClaim("username").asString();*/

            //Token Expired
            if (JwtUtil.isExpired(jwtHaeader_access, "demoApplicationv3")) {
                chain.doFilter(request, response);
                return;
            }
            String userName = JwtUtil.getUserName(jwtHaeader_access, "demoApplicationv3");
            System.out.println("userName :" + userName);
            // 서명이 정상적으로 됨
            if (userName != null) {
                System.out.println("________________________________");
                Users userEntity = userRepository.findByUsername(userName);

                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                //Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                //Security를 저장할 수 있는 세션 공간 -> 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);


            }
        }
        chain.doFilter(request,response);
    }
}
