#!/usr/bin/env bash

JAR=$(find . -name pttg-family-migration-api-*.jar|head)
java -Dcom.sun.management.jmxremote.local.only=false -Djava.security.egd=file:/dev/./urandom -jar "${JAR}"
