FROM openjdk:17-jdk-slim

# Install Python and required dependencies
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    rm -rf /var/lib/apt/lists/*

# Create directory for Python scripts
RUN mkdir -p /app/python_scripts
WORKDIR /app

# Copy Python scripts
COPY src/main/resources/python/* /app/python_scripts/

# Install Python dependencies
RUN pip3 install torch diffusers pillow

# Copy JAR
COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]