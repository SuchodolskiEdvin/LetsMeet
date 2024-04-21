# Stage 1: Build the Vue.js frontend
FROM node:16 as build-stage
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ .
RUN npm run build

# Stage 2: Build the Java Spring Boot application
FROM maven:3.6.3-jdk-11 as build-backend
WORKDIR /backend
COPY backend/pom.xml .
RUN mvn dependency:go-offline
COPY backend/src ./src
# Copy the built Vue.js files into the Spring Boot static directory
COPY --from=build-stage /app/dist /backend/src/main/resources/static
RUN mvn package

# Stage 3: Prepare the final image with the Java application
FROM openjdk:11-jre-slim
COPY --from=build-backend /backend/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
