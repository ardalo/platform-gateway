FROM gradle:jdk11 as builder
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle -q bootJar

FROM adoptopenjdk/openjdk11:alpine-jre
RUN addgroup -S ardalo && adduser -S ardalo -G ardalo
USER ardalo:ardalo
COPY --from=builder /app/build/libs /app

ENTRYPOINT ["java", "-jar", "/app/platform-gateway.jar"]
