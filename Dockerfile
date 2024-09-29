FROM ghcr.io/nico-iaco/graalvm-gradle-docker-build:v1.1.0 as builder
WORKDIR /app

COPY . .

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && gradle -Dspring.profiles.active=default nativeCompile

FROM oraclelinux:9-slim
WORKDIR /app

COPY --from=builder /app/build/native/nativeCompile/grocery-be ./grocery-be

EXPOSE 8080
CMD ["sh", "-c", "/app/grocery-be"]