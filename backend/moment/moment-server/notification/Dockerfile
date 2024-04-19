# 도커 허브에서 공식적으로 제공하는 OpenJDK 이미지를 베이스 이미지로 사용합니다.
FROM openjdk:17-jdk-slim

# 빌드에 필요한 패키지를 설치합니다.
RUN apt-get update && apt-get install -y findutils && rm -rf /var/lib/apt/lists/*

# 작업 디렉토리를 생성합니다.
RUN mkdir -p /app
WORKDIR /app

# Gradle 캐시를 미리 복사하여 의존성 다운로드 시간을 단축합니다.
#COPY .gradle .gradle

# 프로젝트 소스 코드와 Gradle 설정 파일을 복사합니다.
COPY build.gradle gradlew ./
COPY gradle gradle
COPY src src

# Gradle을 이용하여 프로젝트를 빌드합니다.
RUN --mount=type=cache,target=/root/.gradle ./gradlew build -x test

# 빌드 결과물을 Docker 이미지 안에 포함시킵니다. 이건 호스트에서 가져올떄 COPY고 컨테이너 내부에서 빌드한 파일을 복사할때 cp
#COPY build/libs/*SNAPSHOT.jar app.jar
RUN cp build/libs/*SNAPSHOT.jar app.jar


# 컨테이너가 실행될 때 자동으로 실행할 명령어를 설정합니다.
CMD ["java", "-jar", "app.jar"]


