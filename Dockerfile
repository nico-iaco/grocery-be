FROM ghcr.io/graalvm/native-image-community:23-ol9 as builder
WORKDIR /app

COPY . .

RUN ./gradlew -Dspring.profiles.active=default nativeCompile

FROM oraclelinux:9-slim
WORKDIR /app

COPY --from=builder /app/build/native/nativeCompile/grocery-be ./grocery-be

EXPOSE 8080
CMD ["sh", "-c", "/app/grocery-be"]