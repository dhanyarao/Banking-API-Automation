#!/bin/bash
# Start WireMock standalone as a mock Banking API
# Port: 8089 (avoids conflict with common 8080 apps)

set -e
PORT=${1:-8089}
ROOT="$(cd "$(dirname "$0")" && pwd)"
JAR="$ROOT/wiremock-standalone.jar"
VERSION="3.9.1"

if [ ! -f "$JAR" ]; then
  echo "Downloading WireMock standalone ${VERSION}..."
  curl -L -o "$JAR" \
    "https://repo1.maven.org/maven2/org/wiremock/wiremock-standalone/${VERSION}/wiremock-standalone-${VERSION}.jar"
fi

echo "Starting WireMock on http://localhost:${PORT}"
echo "  Root dir : $ROOT"
echo "  Mappings : $ROOT/mappings"
echo ""
echo "Point your tests to:  BASE_URL=http://localhost:${PORT}"
echo "Press Ctrl+C to stop."
echo ""

java -jar "$JAR" \
  --port "$PORT" \
  --root-dir "$ROOT" \
  --global-response-templating \
  --verbose
