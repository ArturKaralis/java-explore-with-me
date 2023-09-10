FROM amazoncorretto:11-alpine-jdk
COPY target/client-*.jar main.jar
ENTRYPOINT ["java","-jar","/main.jar"]