package com.teamh.khumon.dto;

import java.util.Map;

public class GoogleUserInformation extends Oauth2UserInformation{

    private String id;
    private String nickname;
    public GoogleUserInformation(Map<String, Object> attributes) {
        super(attributes);
        this.id = (String) attributes.get("sub");
        this.nickname = (String) attributes.get("name");

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
