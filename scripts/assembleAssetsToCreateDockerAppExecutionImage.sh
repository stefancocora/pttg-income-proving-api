#!/usr/bin/env bash

mkdir build || true
mkdir build/docker || true
docker cp pttg-family-migration-api-build:/code/build/build/libs/${1} build/docker/
cp src/main/resources/run.sh build/docker/
cp src/main/docker/Dockerfile build/docker/
