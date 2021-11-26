FROM openjdk:11

COPY . /app

WORKDIR /app

RUN mkdir config
RUN ./gradlew shadowJar

CMD ["java", "-jar", "OpenPackagedBot-1.0-all.jar"]