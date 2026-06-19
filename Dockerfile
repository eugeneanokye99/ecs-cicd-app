# ── Build stage ──────────────────────────────────────────────────────────────
FROM amazoncorretto:21-al2023-jdk AS build
WORKDIR /app

RUN yum install -y maven && yum clean all

# Copy POM first so dependency layer is cached separately from source
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Runtime stage ─────────────────────────────────────────────────────────────
FROM amazoncorretto:21-al2023
WORKDIR /app

# Non-root user for least-privilege container execution
RUN useradd -r -s /sbin/nologin appuser
USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
