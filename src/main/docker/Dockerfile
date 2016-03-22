FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD /home/ubuntu/jenkins/uk.gov.digital.ho.proving.income.api-0.1.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
