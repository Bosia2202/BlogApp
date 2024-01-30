FROM maven:3.8.7 as build
COPY src src
COPY pom.xml pom.xml
RUN mvn clean package dependency:copy-dependencies -DincludeScope=runtime

FROM eclipse-temurin:17-jdk-alpine
RUN adduser --system Polonius && addgroup --system poloniusGroup && adduser Polonius poloniusGroup
USER Polonius
EXPOSE 8080
WORKDIR /app
COPY --from=build target/*.jar ./application.jar
ENTRYPOINT ["java","-jar","./application.jar"]