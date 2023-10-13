package com.teamh.khumon.dto;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import com.teamh.khumon.security.OAuth2Attributes;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Map;
import java.util.Optional;

public class NaverUserInformation extends Oauth2UserInformation {

    private String id;
    private String nickname;


    public NaverUserInformation(Map<String, Object> attributes) {
        super(attributes);
        Optional<Map<String, Object>> response = Optional.ofNullable( (Map<String, Object>) attributes.get("response"));
        this.id = response.map(stringObjectMap -> (String) stringObjectMap.get("id")).orElse(null);
        this.nickname = response.map(stringObjectMap -> (String) stringObjectMap.get("nickname")).orElse(null);
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
