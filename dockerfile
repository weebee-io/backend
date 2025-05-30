# 1. 앱 빌드
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# 메이븐 config 복사
COPY ./pom.xml ./pom.xml

# 모든 dependency 다운로드
RUN mvn dependency:go-offline -B

# 소스코드 복사
COPY ./src ./src

# 앱 빌드
RUN mvn package -DskipTests

# 2. 앱 실행
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# builder에서 빌드한 jar 파일 복사
COPY --from=builder /app/target/*.jar app.jar

# 환경변수 설정
ENV LOG_PATH=/app/logs
ENV SPRING_PROFILES_ACTIVE=prod

# 로그 디렉토리 생성
RUN mkdir -p /app/logs

# 포트 노출
EXPOSE 8080

# 앱 실행
ENTRYPOINT ["java", "-jar", "app.jar"]