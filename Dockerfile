FROM     openjdk:17-jdk
ARG      JAR_FILE=/build/libs/KHUMON-0.0.1-SNAPSHOT.jar
ADD      ${JAR_FILE} KHUMON.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "KHUMON.jar"]