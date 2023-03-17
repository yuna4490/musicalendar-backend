FROM adoptopenjdk:11-jdk-hotspot
COPY build/libs/*.jar app.jar
EXPOSE 5000
ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y tzdata && \
        echo $TZ > /etc/timezone && \
        ln -snf /usr/share/zoneinfo/$TZ /etc/localtime
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=ebprod","/app.jar"]