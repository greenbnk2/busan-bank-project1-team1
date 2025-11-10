# 1. Java 21(build.gradle 기준) 이미지를 불러옵니다.
FROM openjdk:21-jdk-slim

# 2. 작업 디렉터리를 /app으로 설정
WORKDIR /app

# 3. Gradle 빌드에 필요한 파일들을 먼저 복사 (캐시 활용)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 4. gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 5. 프로젝트 빌드 (JAR 파일 생성)
# gradlew 파일이 있으므로 이것을 사용합니다.
RUN ./gradlew build -x test

# 6. 소스 코드를 복사
COPY src ./src

# 7. 빌드된 JAR 파일을 app.jar로 복사
COPY build/libs/*.jar app.jar

# 8. 이 Docker 이미지가 실행될 때 실행할 명령어
ENTRYPOINT ["java", "-jar", "/app/app.jar"]