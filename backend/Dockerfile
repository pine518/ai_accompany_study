FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
RUN mvn -q -DskipTests package -pl forge-admin-server -am

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/forge-admin-server/target/forge-admin-server.jar app.jar
EXPOSE 8580
ENTRYPOINT ["java", "-jar", "app.jar"]
