FROM eclipse-temurin:21-jre-jammy
VOLUME /tmp
ARG JAR_FILE=stat-svc/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]