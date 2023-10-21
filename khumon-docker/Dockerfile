FROM     openjdk:17-jdk
ARG      JAR_FILE=/build/libs/KHUMON-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ADD      ${JAR_FILE} KHUMON.jar
ENTRYPOINT ["java", "-jar", "KHUMON.jar"]