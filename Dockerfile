# Prefer JDK 22 in Docker.
# If you need to fall back to Java 17, comment the 22 line and uncomment the 17 line.
FROM eclipse-temurin:22-jdk AS build
# FROM eclipse-temurin:17-jdk AS build

ARG MAVEN_VERSION=3.8.8

RUN apt-get update \
  && apt-get install -y --no-install-recommends ca-certificates curl tar \
  && rm -rf /var/lib/apt/lists/* \
  && curl -fsSL "https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz" -o /tmp/maven.tgz \
  && mkdir -p /opt/maven \
  && tar -xzf /tmp/maven.tgz -C /opt/maven --strip-components=1 \
  && rm -f /tmp/maven.tgz

ENV MAVEN_HOME=/opt/maven
ENV PATH="${MAVEN_HOME}/bin:${PATH}"

WORKDIR /workspace

# Copy only what Maven needs first (better layer caching)
COPY pom.xml .

RUN mvn -q -DskipTests dependency:go-offline

# Now copy sources and build the bootable jar
COPY src/ src/
RUN mvn -q -DskipTests package
RUN set -e; \
    jar="$(ls -1 target/*.jar | grep -vE '(^|/)original-|\\.original$' | head -n 1)"; \
    test -n "$jar"; \
    cp "$jar" /workspace/app.jar


# Prefer JDK 22 in runtime too (simpler, guaranteed tag).
# If you need to fall back to Java 17, comment the 22 line and uncomment the 17 line.
FROM eclipse-temurin:22-jdk
# FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /workspace/app.jar /app/app.jar

ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
