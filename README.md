# ecs-cicd-app

Spring Boot application for the ECS CI/CD lab — Eugene Anokye.

Displays:
- **Name:** Eugene Anokye
- **Lab:** ECS CI/CD

---

## Tech Stack

- Java 21, Spring Boot 3.3, Thymeleaf
- Docker (multi-stage build, non-root user)
- GitHub Actions + AWS OIDC for CI/CD

---

## Local Development

```bash
# Build and run locally
mvn spring-boot:run

# Open http://localhost:8080
```

```bash
# Build Docker image locally
docker build -t ecs-cicd-app:local .
docker run -p 8080:8080 ecs-cicd-app:local
```

---

## CI/CD Pipeline

On every push to `main`:

1. **GitHub Actions** builds the JAR and Docker image
2. Image is tagged with the **git SHA** (immutable, never `latest`)
3. Image is pushed to **Amazon ECR** using **OIDC** (no long-lived secrets)
4. The `appspec.yaml` + `taskdef.json` deploy bundle is uploaded to **S3**
5. **EventBridge** detects the ECR push and triggers **CodePipeline**
6. **CodeDeploy** performs a **blue/green deployment** to the ECS service

---

## GitHub Actions Setup

Add the following **repository secret**:

| Secret | Value |
|--------|-------|
| `AWS_ROLE_ARN` | OIDC role ARN from the `ecs-cicd-master` CloudFormation stack output `GitHubOIDCRoleArn` |

The workflow in [.github/workflows/build.yml](.github/workflows/build.yml) uses `permissions: id-token: write` to request short-lived OIDC tokens — no AWS access keys are stored in GitHub.

---

## Image Tag Strategy

Images are tagged with the full **git commit SHA** (`github.sha`):

```
123456789.dkr.ecr.eu-central-1.amazonaws.com/ecs-cicd-app:a3b9c42d...
```

The ECR repository uses **immutable tags** — once pushed, a tag cannot be overwritten. This ensures every deployment is traceable to an exact commit.
