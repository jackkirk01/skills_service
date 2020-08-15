FROM openjdk:11.0.8-jdk-slim

ENV CLASSPATH /opt/lib

WORKDIR /opt

EXPOSE 8080 8091

# copy pom.xml and wildcards to avoid this command failing if there's no target/lib directory
COPY . /opt

RUN ./mvnw clean install -DskipTests

WORKDIR /opt/target

RUN mv *.jar app.jar

CMD ["java","-jar","app.jar"]