FROM amazoncorretto:11-alpine-jdk
COPY target/server-*.jar main.jar
ENTRYPOINT ["java","-jar","/main.jar"]