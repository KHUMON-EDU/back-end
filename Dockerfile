FROM     openjdk:17-jdk
ARG      JAR_FILE=/build/libs/KHUMON-0.0.1-SNAPSHOT.jar

ENV      JWT_SECRET=${JWT_SECRET}
ENV      JWT_EXPIRATION=${ JWT_EXPIRATION  }
ENV      JWT_HEADER=${ JWT_HEADER }
ENV      DB_URL=${ DB_URL }
ENV      DB_USERNAME=${ DB_USERNAME }
ENV      DB_DRIVER=${ DB_DRIVER }
ENV      DB_PASSWORD=${ DB_PASSWORD }
ENV      GOOGLE_CLIENT_ID=${ GOOGLE_CLIENT_ID }
ENV      GOOGLE_CLIENT_SECRET=${ GOOGLE_CLIENT_SECRET }
ENV      GOOGLE_CLIENT_SCOPE=${ GOOGLE_CLIENT_SCOPE }
ENV      GOOGLE_REDIRECT_URI=${ GOOGLE_REDIRECT_URI }
ENV      KAKAO_AUTHORIZATION_GRANT_CODE=${ KAKAO_AUTHORIZATION_GRANT_CODE }
ENV      KAKAO_AUTHORIZATION_URI=${ KAKAO_AUTHORIZATION_URI }
ENV      KAKAO_CLIENT_AUTHENTICATION_METHOD=${ KAKAO_CLIENT_AUTHENTICATION_METHOD }
ENV      KAKAO_CLIENT_ID=${ KAKAO_CLIENT_ID }
ENV      KAKAO_CLIENT_SECRET=${ KAKAO_CLIENT_SECRET }
ENV      KAKAO_REDIRECT_URI=${ KAKAO_REDIRECT_URI }
ENV      KAKAO_SCOPE=${ KAKAO_SCOPE }
ENV      KAKAO_TOKEN_URI=${ KAKAO_TOKEN_URI }
ENV      KAKAO_USER_INFO_URI=${ KAKAO_USER_INFO_URI }
ENV      KAKAO_USER_NAME_ATTRIBUTE=${ KAKAO_USER_NAME_ATTRIBUTE }
ENV      NAVER_AUTHORIZATION_GRANT_TYPE=${ NAVER_AUTHORIZATION_GRANT_TYPE }
ENV      NAVER_AUTHORIZATION_URI=${ NAVER_AUTHORIZATION_URI }
ENV      NAVER_CLIENT_ID=${ NAVER_CLIENT_ID }
ENV      NAVER_CLIENT_SECRET=${ NAVER_CLIENT_SECRET }
ENV      NAVER_REDIRECT_URI=${ NAVER_REDIRECT_URI }
ENV      NAVER_SCOPE=${ NAVER_SCOPE }
ENV      NAVER_TOKEN_URI=${ NAVER_TOKEN_URI }
ENV      NAVER_USER_INFO_URI=${ NAVER_USER_INFO_URI }}
ENV      NAVER_USER_NAME_ATTRIBUTE=${ NAVER_USER_NAME_ATTRIBUTE }
ENV      REDIS_HOST=${ REDIS_HOST }
ENV      REDIS_PORT=${ REDIS_PORT }


ADD      ${JAR_FILE} KHUMON.jar
ENTRYPOINT ["java", "-jar", "KHUMON.jar"]