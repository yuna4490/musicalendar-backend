FROM adoptopenjdk:11-jdk-hotspot
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 5000
ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y tzdata && \
        echo $TZ > /etc/timezone && \
        ln -snf /usr/share/zoneinfo/$TZ /etc/localtime
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=ebprod","/app.jar"]