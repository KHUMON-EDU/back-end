package com.teamh.khumon.dto;

import java.util.Map;
import java.util.Optional;

public class KakaoUserInformation extends Oauth2UserInformation{

    private String id;
    private String nickname;

    public KakaoUserInformation(Map<String, Object> attributes) {
        super(attributes);
        this.id = String.valueOf(attributes.get("id"));
        Optional<Map<String, Object>> account = Optional.ofNullable((Map<String, Object>) attributes.get("kakao_account"));
        this.nickname = account.map(stringObjectMap -> (Map<String, Object>) account.get().get("profile")).map(stringObjectMap -> (String) stringObjectMap.get("nickname")).orElse(null);
    }

    @Override
    public String getIdentifierKey() {
        return this.id;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }
}
