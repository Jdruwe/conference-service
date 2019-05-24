#FROM openjdk:8-jdk-alpine
#VOLUME /tmp
#ADD /build/libs/conference-0.0.1-SNAPSHOT.jar app.jar
#RUN adduser -D myuser
#USER myuser
#CMD java -Dserver.port=$PORT -jar app.jar


FROM openjdk:13-jdk-alpine
VOLUME /tmp
COPY target/conference-0.0.1-SNAPSHOT.jar conference-0.0.1-SNAPSHOT.jar
CMD ["java”,”-Djava.security.egd=file:/dev/./urandom”,”-jar”,”/conference-0.0.1-SNAPSHOT.jar"]
