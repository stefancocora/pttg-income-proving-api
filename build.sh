#!/usr/bin/env bash
set -e
[ -n "${DEBUG}" ] && set -x

GIT_COMMIT=${GIT_COMMIT:-$(git rev-parse --short HEAD)}

build() {
  ./gradlew "${@}"
}

build "build"
