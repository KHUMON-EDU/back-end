package com.teamh.khumon.security;



import com.teamh.khumon.domain.Member;
import com.teamh.khumon.domain.OAuth2Provider;
import com.teamh.khumon.dto.GoogleUserInformation;
import com.teamh.khumon.dto.KakaoUserInformation;
import com.teamh.khumon.dto.NaverUserInformation;
import com.teamh.khumon.dto.Oauth2UserInformation;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Getter
@Slf4j
public class OAuth2Attributes {

    private String nameAttributeKey;
    private Oauth2UserInformation oauth2UserInfo;

    @Builder
    public OAuth2Attributes(String nameAttributeKey, Oauth2UserInformation oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }


    public static OAuth2Attributes of(OAuth2Provider socialType,
                                      String userNameAttributeName, Map<String ,Object> attributes) {

        if (socialType == OAuth2Provider.NAVER) {
            log.info("네이버 로그인임");
            return ofNaver(userNameAttributeName, attributes);
        }
        if (socialType == OAuth2Provider.KAKAO) {
            log.info("카카오 로그인임");
            return ofKakao(userNameAttributeName, attributes);
        }
        log.info("구글 로그인임");
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String ,Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoUserInformation(attributes))
                .build();
    }

    public static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleUserInformation(attributes))
                .build();
    }

    public static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        log.info("attributes : " + attributes.toString());
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverUserInformation(attributes))
                .build();
    }


    public Member toEntity(OAuth2Provider oAuth2Provider, Oauth2UserInformation oauth2UserInfo) {
        return Member.builder()
                .oAuth2Provider(oAuth2Provider)
                .username(oauth2UserInfo.getIdentifierKey())
                .nickname(oauth2UserInfo.getNickname())
                .role("ROLE_USER")
                .build();
    }
}
