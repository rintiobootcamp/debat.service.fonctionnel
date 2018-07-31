FROM openjdk:8-jdk-alpine
ADD target/debatService.jar ws_debatService_sf.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","ws_debatService_sf.jar"]