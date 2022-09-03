FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-22 as builder
WORKDIR /app

# For SDKMAN to work we need unzip & zip
RUN yum install -y unzip zip

RUN \
    # Install SDKMAN
    curl -s "https://get.sdkman.io" | bash; \
    source "$HOME/.sdkman/bin/sdkman-init.sh"; \
    sdk install gradle; \
    # Install GraalVM Native Image
    gu install native-image;

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && gradle --version

RUN native-image --version

COPY . .

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && gradle nativeCompile

FROM oraclelinux:7-slim
WORKDIR /app

COPY --from=builder /app/build/native/nativeCompile/grocery-be ./grocery-be

EXPOSE 8080
CMD ["sh", "-c", "/app/grocery-be"]