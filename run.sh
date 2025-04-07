#!/bin/sh

set -e

(
  cd "$(dirname "$0")"
  mvn -B package -Ddir=/tmp/codecrafters-build-dns-server-java-dns
)

exec java -jar /tmp/codecrafters-build-dns-server-java-dns/codecrafters-dns-server.jar "$@"
