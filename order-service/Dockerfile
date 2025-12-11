FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# copy maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# download dependencies
RUN ./mvnw dependency:go-offline -B

# copy source code
COPY src ./src

# build application
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# expose port
EXPOSE 8080

# run application
ENTRYPOINT ["java", "-jar", "app.jar"]
