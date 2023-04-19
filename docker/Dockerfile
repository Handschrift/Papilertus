FROM openjdk:17-slim-bullseye
RUN apt update && apt upgrade -y
RUN apt install git -y
WORKDIR /papilertus
RUN git clone https://github.com/Handschrift/Papilertus.git .
RUN ./gradlew shadowJar
CMD ["java", "-jar", "build/libs/Papilertus-1.0-all.jar"]