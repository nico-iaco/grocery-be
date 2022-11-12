FROM ghcr.io/nico-iaco/graalvm-gradle-docker-build:v1.0.0 as builder
WORKDIR /app

COPY . .

ARG PROJECT_ID

ENV projectId=${PROJECT_ID}

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && gradle nativeCompile

FROM oraclelinux:7-slim
WORKDIR /app

COPY --from=builder /app/build/native/nativeCompile/grocery-be ./grocery-be

EXPOSE 8080
CMD ["sh", "-c", "/app/grocery-be"]