# Stage 1: Build
# OpenJDK 17 JDK 이미지를 사용하여 빌드 환경을 설정합니다.
FROM openjdk:17-jdk-slim as build

# 작업 디렉토리를 설정합니다.
WORKDIR /app

# 프로젝트 소스 코드를 모두 컨테이너로 복사합니다.
COPY . ./

# Gradle Wrapper를 실행하여 프로젝트를 빌드합니다.
# '--no-daemon' 옵션은 데몬 사용을 비활성화하여 빌드 속도를 높입니다.
# 'bootJar' 태스크로 실행 가능한 JAR 파일을 생성합니다.
RUN ./gradlew bootJar --no-daemon

# Stage 2: Run
# JRE 17 이미지를 사용하여 애플리케이션 실행 환경을 만듭니다.
# JRE 이미지는 JDK 이미지보다 훨씬 가볍습니다.
FROM openjdk:17-jdk-slim

# JAR 파일을 복사할 디렉토리를 설정합니다.
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일을 복사합니다.
# /app/build/libs/ 디렉토리에서 *.jar 파일을 /app/app.jar로 복사합니다.
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 컨테이너가 사용할 포트를 외부에 노출합니다.
EXPOSE 8080

# 컨테이너 시작 시 실행될 명령어를 정의합니다.
# 별도의 최적화 옵션 없이, 가장 기본적인 'java -jar' 명령어로 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]