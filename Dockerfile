# ── Build stage ──────────────────────────────────────────────────────────────
# Official Maven image with Amazon Corretto 21 — no manual Maven install needed
FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /app

# Copy POM first so dependency layer is cached separately from source
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Runtime stage ─────────────────────────────────────────────────────────────
FROM amazoncorretto:21-al2023
WORKDIR /app

# shadow-utils provides useradd on Amazon Linux 2023
RUN yum install -y shadow-utils && \
    useradd -r -s /sbin/nologin appuser && \
    yum clean all -y

USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
