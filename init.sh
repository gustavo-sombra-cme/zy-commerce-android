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

read_manifest_files() {
  ruby -rjson -e '
    key = ARGV.fetch(0)
    data = JSON.parse(File.read(".harness/harness_manifest.json"))
    data.fetch(key).each { |file| puts file }
  ' "$1"
}

required_files=()
while IFS= read -r file; do
  required_files+=("$file")
done < <(read_manifest_files "startup_required_files")

echo "Checking required harness files..."
missing_files=0
for file in "${required_files[@]}"; do
  if [[ -f "$file" ]]; then
    echo "  OK: $file"
  else
    echo "  MISSING: $file" >&2
    missing_files=1
  fi
done

if [[ "$missing_files" -ne 0 ]]; then
  echo "Harness bootstrap failed because required files are missing." >&2
  exit 1
fi

echo "Feature summary:"
ruby -rjson -e '
data = JSON.parse(File.read("feature_list.json"))
features = data.fetch("features")
active = features.select { |feature| feature["status"] == "in_progress" }
if active.empty?
  next_feature = features.find { |feature| feature["status"] == "planned" }
  if next_feature
    puts "- next: #{next_feature["id"]}: #{next_feature["name"]} [#{next_feature["status"]}]"
  else
    puts "- no planned or in-progress feature found"
  end
else
  active.each do |feature|
    puts "- active: #{feature["id"]}: #{feature["name"]} [#{feature["status"]}]"
  end
end
'

echo "Running standard Gradle verification..."
./gradlew test
./gradlew lintDebug
./gradlew assembleDebug

echo "Running harness cleanup scanner..."
bash scripts/cleanup-scanner.sh
