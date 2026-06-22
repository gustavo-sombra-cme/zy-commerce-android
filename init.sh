#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

echo "ZY-Commerce Android bootstrap"
echo "Working directory: $(pwd)"

if [[ -z "${JAVA_HOME:-}" ]]; then
  if [[ -d "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home" ]]; then
    export JAVA_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
  fi
fi

if [[ -z "${JAVA_HOME:-}" ]]; then
  echo "JAVA_HOME is not set. Install or select JDK 17 before running Gradle." >&2
  exit 1
fi

echo "Using JAVA_HOME=$JAVA_HOME"

echo "Active feature summary:"
ruby -rjson -e 'data=JSON.parse(File.read("feature_list.json")); data.fetch("features").select { |f| f["status"] == "in_progress" || f["id"] == "ST-01" }.each { |f| puts "- #{f["id"]}: #{f["name"]} [#{f["status"]}]" }'

./gradlew test
./gradlew lintDebug
./gradlew assembleDebug
