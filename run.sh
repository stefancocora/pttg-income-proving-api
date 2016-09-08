#!/usr/bin/env bash
NAME=${NAME:-pttg-ip-api}

JAR=$(find . -name ${NAME}*.jar|head -1)
java -Dcom.sun.management.jmxremote.local.only=false -Djava.security.egd=file:/dev/./urandom -jar "${JAR}"
