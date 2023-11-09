package com.teamh.khumon.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    private static final String jwtSchemeName = "Authorization";
    private static final String scheme = "Bearer";


    @Bean
    public OpenAPI openAPI(){
        Server server = new Server();
        server.setUrl("https://khumon-edu.kro.kr");
        List<Server> servers = new ArrayList<>();
        servers.add(server);
        return new OpenAPI()
                .components(components()).addSecurityItem(securityRequirement()).servers(servers).info(apiInfo());

    }


    private Components components(){
        return new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .scheme(scheme)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)
    }

    private Info apiInfo() {
        String title = "KHUMON EDUCATION";
        String description = "KHUMON EDUCATION API Documentation";
        return new Info()
                .title(title)
                .description(description)
                .version("1.0");
    }


    private SecurityRequirement securityRequirement(){
        String jwtSchemeName = "Authorization";
        return new SecurityRequirement().addList(jwtSchemeName);
    }

}
