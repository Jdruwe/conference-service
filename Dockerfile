FROM openjdk:13-jdk-alpine
VOLUME /tmp
ADD /build/libs/conference-service-0.0.1-SNAPSHOT.jar app.jar
RUN adduser -D myuser
USER myuser
CMD java -Dserver.port=$PORT -jar app.jar
