FROM openjdk:21-jdk-slim

WORKDIR /app

COPY did-wallet-server/gradlew .
COPY did-wallet-server/gradle gradle
COPY did-wallet-server/build.gradle .
COPY did-wallet-server/settings.gradle .
COPY did-wallet-server/libs libs
COPY did-wallet-admin /did-wallet-admin
COPY did-wallet-server/src src

RUN chmod +x ./gradlew

ENV GRADLE_OPTS="-Xmx2g -XX:MaxMetaspaceSize=512m"
RUN ./gradlew bootJar --no-daemon --max-workers=1

CMD ["java", "-jar", "/app/build/libs/did-wallet-server-2.0.0.jar"]