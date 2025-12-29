FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# copy pom first for better caching
COPY pom.xml .

# download dependencies
RUN mvn dependency:go-offline -B

# copy source code
COPY src ./src

# build application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# expose port (matches application.properties)
EXPOSE 8085

# run application
ENTRYPOINT ["java", "-jar", "app.jar"]

