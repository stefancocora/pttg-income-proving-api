FROM quay.io/ukhomeofficedigital/openjdk8:v1.0.0

ENV MONGO_HOST localhost
ENV MONGO_PORT 28017
ENV HMRC_API_ENDPOINT localhost
ENV USER pttg
ENV GROUP pttg
ENV NAME pttg-ip-api

ARG JAR_PATH
ARG VERSION

WORKDIR /app

RUN groupadd -r ${GROUP} && \
    useradd -r -g ${USER} ${GROUP} -d /app && \
    mkdir -p /app && \
    chown -R ${USER}:${GROUP} /app

COPY ${JAR_PATH}/${NAME}-${VERSION}.jar /app
COPY run.sh /app

RUN chmod a+x /app/run.sh

EXPOSE 8081

USER pttg

ENTRYPOINT /app/run.sh
