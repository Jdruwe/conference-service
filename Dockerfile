#FROM openjdk:11-jdk-alpine
#VOLUME /tmp
#ADD /build/libs/conference-0.0.1-SNAPSHOT.jar app.jar
#RUN adduser -D myuser
#USER myuser
#CMD java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar app.jar

FROM openjdk:13-jdk-alpine
VOLUME /tmp
ADD /build/libs/conference-0.0.1-SNAPSHOT.jar app.jar
RUN adduser -D myuser
USER myuser
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
