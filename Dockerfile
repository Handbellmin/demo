FROM adoptopenjdk/openjdk11
CMD ["clean", "package"]
ARG WAR_FILE_PATH=target/*.war
COPY ${WAR_FILE_PATH} app.war
