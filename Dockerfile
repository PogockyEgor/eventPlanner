FROM openjdk:19
RUN mkdir /app
WORKDIR /app
COPY target/eventPlanner-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT java -jar /app/eventPlanner-0.0.1-SNAPSHOT.jar