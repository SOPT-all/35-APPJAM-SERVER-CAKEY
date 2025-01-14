FROM amd64/amazoncorretto:17
COPY cakey-api/build/libs/cakey-api-0.0.1-SNAPSHOT.jar CAKEY.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "CAKEY.jar"]
