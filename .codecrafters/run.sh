#!/bin/sh

set -e

exec java -jar /tmp/codecrafters-build-dns-server-java-dns/codecrafters-dns-server.jar "$@"
