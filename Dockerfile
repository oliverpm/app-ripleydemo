FROM openjdk:11
COPY "./target/HolaRipley-0.0.1-SNAPSHOT.jar" "AppHolaRipley.jar"
EXPOSE 8085
ENTRYPOINT ["java","-jar","AppHolaRipley.jar"]