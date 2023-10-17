package com.teamh.khumon.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtProviderService jwtProviderService;

    private final RedisService redisService;

    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String getAccessToken = jwtProviderService.extractAccessToken(httpServletRequest).orElseThrow();
        String OAuth2Id = jwtProviderService.extractOAuth2Id(getAccessToken);
        Long expiration = jwtProviderService.getExpireTime(getAccessToken);
        redisService.setDataWithExpiration(getAccessToken, "BLACKLIST_" + OAuth2Id, expiration);
    }
}
