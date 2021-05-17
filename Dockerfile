FROM gradle:jdk15 as builder
WORKDIR /project
COPY src ./src
COPY build.gradle.kts ./build.gradle.kts
RUN ./gradlew createDocker



FROM openjdk:15-slim
WORKDIR /root
COPY --from=builder /project/build/libs/*.jar ./app
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "/root/app"]