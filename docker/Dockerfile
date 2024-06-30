FROM openjdk:21-oracle AS builder
WORKDIR /app
COPY target/burdok-FINAL.jar burdok-FINAL.jar
RUN java -Djarmode=layertools -jar burdok-FINAL.jar extract

FROM openjdk:21-oracle
WORKDIR /app
EXPOSE 80
RUN ls -R /app
COPY --from=builder /app/dependencies/ ./
RUN ls -R /app
COPY --from=builder /app/snapshot-dependencies/ ./
RUN ls -R /app
COPY --from=builder /app/spring-boot-loader/ ./
RUN ls -R /app
COPY --from=builder /app/application/ ./
RUN ls -R /app
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "org.springframework.boot.loader.launch.JarLauncher"]