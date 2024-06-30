FROM openjdk:21-oracle AS builder
WORKDIR /app
COPY target/burdok-FINAL.jar burdok-FINAL.jar
RUN java -Djarmode=layertools -jar burdok-FINAL.jar extract

FROM openjdk:21-oracle
WORKDIR /app
EXPOSE 80
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/application/ ./
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "org.springframework.boot.loader.launch.JarLauncher"]