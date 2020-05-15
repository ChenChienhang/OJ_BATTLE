FROM java:8
VOLUME /tmp
ADD ojbattle-1.0-SNAPSHOT.jar /test.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/test.jar"]