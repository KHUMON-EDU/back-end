package com.teamh.khumon.security;


import com.teamh.khumon.service.JwtProviderService;

import com.teamh.khumon.service.MemberDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        final Optional<String> jwt = jwtProviderService.extractAccessToken(request);


        if (jwt.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtProviderService.validateToken(jwt.get())) {
            String OAuth2Id = jwtProviderService.extractOAuth2Id(jwt.get());
            UserDetails userDetails = memberDetailsService.loadUserByUsername(OAuth2Id);
            Authentication authentication = jwtProviderService.getAuthentication(userDetails);

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

        }

        filterChain.doFilter(request, response);

    }
}
