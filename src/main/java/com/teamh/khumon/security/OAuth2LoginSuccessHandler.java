package com.teamh.khumon.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamh.khumon.domain.Member;
import com.teamh.khumon.dto.CustomOAuth2User;
import com.teamh.khumon.repository.MemberRepository;
import com.teamh.khumon.service.JwtProviderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProviderService jwtProviderService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("소셜 로그인 성공");
        String username = extractUsername(authentication);
        String accessToken = jwtProviderService.generateAccessToken(username, "ROLE_USER");


        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        Member member = memberRepository.findByUsername(username).get();

        String uri = createURI(accessToken, member.getId(), member.getNickname()).toString();
        response.sendRedirect(uri);   // sendRedirect() 메서드를 이용해 Frontend 애플리케이션 쪽으로 리다이렉트
    }


    private String extractUsername(Authentication authentication) {
        log.info(authentication.toString());
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        log.info(oAuth2User.getUsername());
        return oAuth2User.getUsername();
    }

    private URI createURI(String accessToken,  Long userId, String nickname) throws UnsupportedEncodingException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("id", String.valueOf(userId));
        queryParams.add("access-token", accessToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port(3000)
                .path("/#/oauth2/login")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
