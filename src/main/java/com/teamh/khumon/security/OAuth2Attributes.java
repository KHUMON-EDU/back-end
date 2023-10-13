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

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private Oauth2UserInformation oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuth2Attributes(String nameAttributeKey, Oauth2UserInformation oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
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



    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public Member toEntity(OAuth2Provider oAuth2Provider, Oauth2UserInformation oauth2UserInfo) {
        return Member.builder()
                .oAuth2Provider(oAuth2Provider)
                .username(oauth2UserInfo.getIdentifierKey())
                .nickname(oauth2UserInfo.getNickname())
                .role("ROLE_USER")
                .build();
    }
}
