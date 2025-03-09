#FROM openjdk:21
#COPY . /usr/src/myapp
#WORKDIR /usr/src/myapp
#RUN javac RabbitArnabApplication.java
#CMD ["java", "Main"]



FROM openjdk:21
ARG JAR_FILE=./rabbit-sequoia-spring/target/rabbit-arnab-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]