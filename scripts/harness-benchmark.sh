#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== Harness Benchmark ==="

missing=0
warnings=0

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
done < <(read_manifest_files "benchmark_required_files")

for file in "${required_files[@]}"; do
  if [[ -f "$file" ]]; then
    echo "[files] OK: $file"
  else
    echo "[files] FAIL: Missing required file: $file"
    missing=1
  fi
done

if [[ ! -f "./gradlew" ]]; then
  echo "[gradle] FAIL: Missing Gradle wrapper"
  missing=1
else
  echo "[gradle] OK: Gradle wrapper present"
fi

feature_state="$(python3 - <<'PY2'
import json
from pathlib import Path

data = json.loads(Path('feature_list.json').read_text())
features = data['features']
active = [feature for feature in features if feature['status'] == 'in_progress']
selected = active[0] if active else next((feature for feature in features if feature['status'] == 'planned'), None)
if selected:
    print('|'.join([selected['id'], selected['name'], selected['epic'], 'active' if active else 'next']))
else:
    print('|||')
PY2
)"
IFS='|' read -r selected_id selected_name selected_epic selected_kind <<< "$feature_state"
if [[ -n "$selected_id" ]]; then
  echo "[scope] OK: ${selected_kind} feature is ${selected_id} - ${selected_name} (${selected_epic})"
else
  echo "[scope] WARN: No active or planned feature found"
  warnings=$((warnings + 1))
fi

echo "[backend] Checking local backend availability..."
backend_output="$(bash scripts/backend-API-check.sh 2>&1)"
echo "$backend_output"
if echo "$backend_output" | grep -q "Backend API is running"; then
  echo "[backend] OK: Backend availability path is healthy for smoke testing"
else
  echo "[backend] WARN: Backend is not currently available; backend-dependent smoke verification would be skipped"
  warnings=$((warnings + 1))
fi

echo "[android] Checking local-network assumptions..."
debug_base_url="$(python3 - <<'PY2'
from pathlib import Path

lines = Path('app/build.gradle.kts').read_text().splitlines()
current = None
value = ''
for raw_line in lines:
    stripped = raw_line.strip()
    if stripped == 'debug {':
        current = 'debug'
        continue
    if current == 'debug' and stripped.startswith('buildConfigField("String", "BASE_URL"'):
        parts = stripped.split('\\"')
        value = parts[1] if len(parts) > 1 else ''
    if current == 'debug' and stripped == '}':
        current = None
print(value)
PY2
)"
if [[ "$debug_base_url" == "http://10.0.2.2:5015/" ]]; then
  echo "[android] OK: Debug BASE_URL matches emulator local backend host"
else
  echo "[android] WARN: Debug BASE_URL is '$debug_base_url'"
  warnings=$((warnings + 1))
fi

if [[ -n "${selected_id:-}" ]]; then
  case "$selected_epic" in
    "Authentication")
      targeted_command="./gradlew :domain:auth:test :data:auth:test :feature:auth:test"
      ;;
    *)
      targeted_command=""
      ;;
  esac
else
  targeted_command=""
fi

if [[ -n "$targeted_command" ]]; then
  if [[ -z "${JAVA_HOME:-}" && -d "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home" ]]; then
    export JAVA_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
  fi

  echo "[targeted] Running focused verification for ${selected_id}: $targeted_command"
  if $targeted_command; then
    echo "[targeted] OK: Focused verification passed"
  else
    echo "[targeted] FAIL: Focused verification failed"
    exit 1
  fi
else
  echo "[targeted] INFO: No lightweight targeted verification is mapped yet for ${selected_id:-current scope}"
fi

echo "[cleanup] Running cleanup scanner..."
bash scripts/cleanup-scanner.sh

if [[ "$missing" -ne 0 ]]; then
  echo "=== Result: FAIL (${warnings} warning(s)) ==="
  exit 1
fi

echo "=== Result: PASS (${warnings} warning(s)) ==="
