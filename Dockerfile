FROM amd64/amazoncorretto:17
WORKDIR /app
COPY ./cakey-api/build/libs/cakey-api-0.0.1-SNAPSHOT.jar /app/CAKEY.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "CAKEY.jar"]
