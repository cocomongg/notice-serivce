# 프로젝트 실행 방법

## 1. 저장소 클론

아래 명령어를 사용하여 Git 저장소를 클론합니다.

```bash
git clone https://github.com/cocomongg/notice-serivce.git
```

## 2. Docker Compose를 통한 MySQL, Redis 실행

### 2.1 Docker Compose 파일 위치

Docker Compose 파일은 다음 경로에 위치해 있습니다:

```
notice-service/docker/app/application-compose.yml
```

### 2.2 컨테이너 빌드 및 실행

터미널에서 해당 디렉토리로 이동한 후, 아래 명령어를 실행하여 MySQL과 Redis 컨테이너를 빌드 및 실행합니다.

```bash
cd notice-service/docker/app
docker compose -f ./application-compose.yml up -d
```

### 2.3 컨테이너 상태 확인

컨테이너가 정상적으로 실행 중인지 확인하려면 다음 명령어를 사용합니다.

```bash
docker ps
```

출력 결과에서 MySQL과 Redis 컨테이너가 "Up" 상태임을 확인하실 수 있습니다. (포트 정보 mysql: 3307, redis: 6380)

> **참고:** Docker Compose 파일에 명시된 포트 번호를 확인 필요

## 3. Spring Boot 애플리케이션 실행

### 3.1 Gradle을 통한 애플리케이션 실행

프로젝트 루트 디렉토리로 이동한 후, 아래 명령어를 사용하여 Spring Boot 애플리케이션을 실행합니다.

```bash
./gradlew bootRun
```

## 4. 테스트 코드 실행

테스트는 Testcontainers를 이용하기 때문에 Docker 데몬이 실행 중이여야 합니다.

### 4.1 Docker 데몬 실행 확인

테스트를 실행하기 전에 Docker 데몬이 실행 중인지 아래 명령어로 확인합니다.

```bash
docker info
```

### 4.2 테스트 실행

Docker 데몬이 실행 중임을 확인한 후, 아래 명령어로 테스트 코드를 실행합니다.

```bash
./gradlew test
```

## 5. API 문서 확인

API 문서는 Swagger를 통해 확인할 수 있습니다.
- 주소: http://localhost:8080/swagger-ui.html
- 참고: [사용자 시나리오](user-scenario.md)
