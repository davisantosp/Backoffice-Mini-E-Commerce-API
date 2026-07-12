FROM maven:3.9.16-eclipse-temurin-21 AS build
WORKDIR /Backoffice-Mini-E-Commerce
COPY ./pom.xml pom.xml
RUN mvn dependency:go-offline
COPY ./src src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-noble AS runner
WORKDIR /Backoffice-Mini-E-Commerce
COPY --from=build /Backoffice-Mini-E-Commerce/target/*.jar ./backoffice-app.jar
ENTRYPOINT ["java", "-jar", "backoffice-app.jar"]
