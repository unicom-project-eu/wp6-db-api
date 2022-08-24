FROM maven:3.8.3-openjdk-17 as build
WORKDIR /app
COPY pom.xml pom.xml
RUN mvn dependency:go-offline
COPY src src
RUN mvn package -DskipTests

FROM gcr.io/distroless/java17 as run
COPY --from=build /app/target/unicom-backend.jar /app/unicom-backend.jar
WORKDIR /app
EXPOSE 8080
CMD ["/app/unicom-backend.jar"]