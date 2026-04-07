#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

# Some Codespaces/extension setups inject this flag, which breaks on Java <= 17.
if [[ "${JAVA_TOOL_OPTIONS:-}" == *"--enable-native-access=ALL-UNNAMED"* ]]; then
  export JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS/--enable-native-access=ALL-UNNAMED/}"
fi
if [[ "${MAVEN_OPTS:-}" == *"--enable-native-access=ALL-UNNAMED"* ]]; then
  export MAVEN_OPTS="${MAVEN_OPTS/--enable-native-access=ALL-UNNAMED/}"
fi

if [[ -f "$ROOT_DIR/backend/pom.xml" ]]; then
  BACKEND_DIR="$ROOT_DIR/backend"
elif [[ -f "$PWD/pom.xml" ]]; then
  BACKEND_DIR="$PWD"
else
  echo "Could not find backend/pom.xml. Run this command from repo root or backend directory." >&2
  exit 1
fi

if [[ -x "$BACKEND_DIR/mvnw" ]]; then
  MVN_CMD="$BACKEND_DIR/mvnw"
elif command -v mvn >/dev/null 2>&1; then
  MVN_CMD="mvn"
else
  echo "Maven not found. Install Maven or add Maven Wrapper (mvnw) to backend/." >&2
  exit 127
fi

cd "$BACKEND_DIR"
"$MVN_CMD" spring-boot:run
