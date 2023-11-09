package com.teamh.khumon.configuration;


import com.teamh.khumon.security.*;
import com.teamh.khumon.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-resources/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/api-docs/**")).permitAll()
                                .anyRequest().permitAll()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHanding -> exceptionHanding.accessDeniedHandler(customAccessDeniedHandler))
                .exceptionHandling(authenticationEntryPoint -> authenticationEntryPoint.authenticationEntryPoint(customAuthenticationEntryPoint))
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
                    httpSecurityOAuth2LoginConfigurer.successHandler(oAuth2LoginSuccessHandler);
                    httpSecurityOAuth2LoginConfigurer.failureHandler(oAuth2LoginFailureHandler);
                    httpSecurityOAuth2LoginConfigurer.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService));
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().requestMatchers(PathRequest.toH2Console());
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080", "http://facerain-dev.iptime.org:5000", "https://khumon-study.kro.kr", "https://khumon-edu.kro.kr"));
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");

        configuration.addAllowedHeader("authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addExposedHeader("Cache-Control");

        configuration.addExposedHeader("authorization");
        configuration.addExposedHeader("Cache-Control");
        configuration.addExposedHeader("Content-Type");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
