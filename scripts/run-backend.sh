#!/usr/bin/env bash
set -euo pipefail

# Some Codespaces/extension setups inject this flag, which breaks on Java <= 17.
if [[ "${JAVA_TOOL_OPTIONS:-}" == *"--enable-native-access=ALL-UNNAMED"* ]]; then
  export JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS/--enable-native-access=ALL-UNNAMED/}"
fi

if [[ "${MAVEN_OPTS:-}" == *"--enable-native-access=ALL-UNNAMED"* ]]; then
  export MAVEN_OPTS="${MAVEN_OPTS/--enable-native-access=ALL-UNNAMED/}"
fi

cd backend
mvn spring-boot:run
