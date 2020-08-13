FROM openjdk:11
COPY target/*.war /usr/src/spring.war
CMD ["java", "-jar", "/usr/src/spring.war"]

