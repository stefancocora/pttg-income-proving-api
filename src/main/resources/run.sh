#!/usr/bin/env bash

java -Dcom.sun.management.jmxremote.local.only=false -Djava.security.egd=file:/dev/./urandom -jar /pttg-family-migration-api-*.jar
