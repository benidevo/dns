FROM eclipse-temurin:23-jdk-alpine as build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN apk add --no-cache maven && \
    mvn package -DskipTests

FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

COPY --from=build /app/target/codecrafters-dns-server.jar .

EXPOSE 2053/udp

CMD ["java", "-jar", "codecrafters-dns-server.jar"]
