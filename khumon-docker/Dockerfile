FROM     openjdk:17-jdk
EXPOSE 8080
ARG      JAR_FILE=../build/libs/KHUMON-0.0.1-SNAPSHOT.jar
ADD      ${JAR_FILE} KHUMON.jar
ENTRYPOINT ["java", "-jar", "KHUMON.jar"]