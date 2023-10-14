package com.teamh.khumon.security;


import com.teamh.khumon.repository.MemberRepository;
import com.teamh.khumon.service.JwtProviderService;

import com.teamh.khumon.service.MemberDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import java.io.IOException;
import java.util.Optional;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;





@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtProviderService jwtProviderService;

    private final MemberDetailsService memberDetailsService;



    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 예외 처리 : 들어온 토큰 값이 올바르지 않은 경우 다른 체인으로 넘어감.
        final Optional<String> jwt = jwtProviderService.extractAccessToken(request);

        // 재인증하지 않기 위해 사용자가 인증되었는지 확인.
        // 로그인은 되어있고 아직 인증은 안된 경우만 if문 내부 접근
        if (jwt.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtProviderService.validateToken(jwt.get())) {
            String OAuth2Id = jwtProviderService.extractOAuth2Id(jwt.get());
            UserDetails userDetails = memberDetailsService.loadUserByUsername(OAuth2Id);
            Authentication authentication = jwtProviderService.getAuthentication(userDetails); //Authentication 객체 생성

            //SecurityContextHolder에 새로운 SecurityContext 영역을 할당하고,
            // 생성한 Authentication 객체를 담는다.
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

        }

        filterChain.doFilter(request, response);

    }
}
