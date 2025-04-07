FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

# Install Python
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    rm -rf /var/lib/apt/lists/* && \
    pip3 install torch diffusers pillow --extra-index-url https://download.pytorch.org/whl/cpu

# Copy from build stage
COPY --from=build /app/target/*.jar app.jar
COPY src/main/resources/python /app/python_scripts

ENTRYPOINT ["java", "-jar", "app.jar"]