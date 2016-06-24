#!/usr/bin/env bash
set -e
[ -n "${DEBUG}" ] && set -x

GRADLE_IMAGE="quay.io/ukhomeofficedigital/gradle:v2.13.5"
GIT_COMMIT=${GIT_COMMIT:-$(git rev-parse --short HEAD)}
GIT_COMMIT=${GIT_COMMIT:0:7}
VERSION="0.1.0"

build() {

  MOUNT="${PWD}:/code"
  # Mount in the local gradle cache into the docker container
  [ -d "${HOME}/.gradle/caches" ] && MOUNT="${MOUNT} -v ${HOME}/.gradle/caches:/root/.gradle/caches"

  # Mount in local maven repository into the docker container
  [ -d "${HOME}/.m2/repository" ] && MOUNT="${MOUNT} -v ${HOME}/.m2/repository:/root/.m2/repository"
  
  # Mount in local gradle user directory
  [ -d "${HOME}/.gradle" ] && MOUNT="${MOUNT} -v ${HOME}/.gradle:/root/.gradle"

  docker run -e GIT_COMMIT=${GIT_COMMIT} -e VERSION=${VERSION} -v ${MOUNT} "${GRADLE_IMAGE}" "${@}"
}

build "${@}"
