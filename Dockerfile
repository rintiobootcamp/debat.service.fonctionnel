FROM openjdk:8-jdk-alpine
ADD target/debatService.jar ws_debatService_sf.jar
EXPOSE 8088
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap  -XX:MaxRAMFraction=1 -XshowSettings:vm "
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar ws_debatService_sf.jar" ]