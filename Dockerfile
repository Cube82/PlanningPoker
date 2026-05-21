FROM eclipse-temurin:21-jdk AS build

ARG ANDROID_CMDLINE_TOOLS_VERSION=14742923
ARG ANDROID_CMDLINE_TOOLS_SHA1=48833c34b761c10cb20bcd16582129395d121b27
ARG ANDROID_COMPILE_SDK=36

ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH="${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:${PATH}"

WORKDIR /workspace

RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl libatomic1 unzip \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p "${ANDROID_HOME}/cmdline-tools" \
    && curl -fsSL "https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_CMDLINE_TOOLS_VERSION}_latest.zip" -o /tmp/cmdline-tools.zip \
    && echo "${ANDROID_CMDLINE_TOOLS_SHA1}  /tmp/cmdline-tools.zip" | sha1sum -c - \
    && unzip -q /tmp/cmdline-tools.zip -d "${ANDROID_HOME}/cmdline-tools" \
    && mv "${ANDROID_HOME}/cmdline-tools/cmdline-tools" "${ANDROID_HOME}/cmdline-tools/latest" \
    && rm /tmp/cmdline-tools.zip

RUN yes | sdkmanager --sdk_root="${ANDROID_HOME}" --licenses >/dev/null
RUN sdkmanager --sdk_root="${ANDROID_HOME}" "platform-tools" "platforms;android-${ANDROID_COMPILE_SDK}"

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew :composeApp:wasmJsBrowserDistribution :server:installDist -PandroidCompileSdk=${ANDROID_COMPILE_SDK} --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/server/build/install/server /app/server
COPY --from=build /workspace/composeApp/build/dist/wasmJs/productionExecutable /app/public

ENV FRONTEND_STATIC_DIR=/app/public
EXPOSE 8080

CMD ["/app/server/bin/server"]
