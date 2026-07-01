#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== Cleanup Scanner ==="

issues=0
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
done < <(read_manifest_files "cleanup_required_files")

for file in "${required_files[@]}"; do
  if [[ -f "$file" ]]; then
    echo "[OK] $file"
  else
    echo "[FAIL] Missing file: $file"
    issues=$((issues + 1))
  fi
done

feature_state="$(python3 - <<'PY2'
import json
from pathlib import Path

data = json.loads(Path('feature_list.json').read_text())
features = data['features']
active = [feature['id'] for feature in features if feature['status'] == 'in_progress']
done = [feature['id'] for feature in features if feature['status'] == 'done']
next_feature = next((feature for feature in features if feature['status'] == 'planned'), None)
active_dependencies = []
for feature in features:
    if feature['id'] in active:
        active_dependencies.extend(feature.get('dependencies', []))
next_dependencies = next_feature.get('dependencies', []) if next_feature else []
print('|'.join([
    ','.join(active),
    str(len(active)),
    next_feature['id'] if next_feature else '',
    next_feature['name'] if next_feature else '',
    ','.join(done),
    ','.join(active_dependencies),
    ','.join(next_dependencies),
]))
PY2
)"
IFS='|' read -r active_ids_csv in_progress_count next_planned_id next_planned_name done_ids_csv active_dependency_ids_csv next_dependency_ids_csv <<< "$feature_state"

contains_csv() {
  local csv="$1"
  local needle="$2"
  [[ ",$csv," == *",$needle,"* ]]
}

if [[ "$in_progress_count" -le 1 ]]; then
  echo "[OK] In-progress feature count: $in_progress_count"
else
  echo "[FAIL] More than one in-progress feature found: $in_progress_count"
  issues=$((issues + 1))
fi

progress_state="$(python3 - <<'PY2'
from pathlib import Path

text = Path('.harness/docs/PROGRESS.MD').read_text().splitlines()
feature_id = ''
status = ''
for line in text:
    if line.startswith('- Feature or task ID:'):
        feature_id = line.split(':', 1)[1].strip()
    if line.startswith('- Status:'):
        status = line.split(':', 1)[1].strip().strip('`')
print(f'{feature_id}|{status}')
PY2
)"
IFS='|' read -r progress_id progress_status <<< "$progress_state"

handoff_state="$(python3 - <<'PY2'
from pathlib import Path

text = Path('.harness/docs/SESSION_HANDOFF.MD').read_text().splitlines()
active = ''
next_planned = ''
for index, line in enumerate(text):
    if line == '## Active Feature' and index + 2 < len(text):
        active = text[index + 2].removeprefix('- ').replace('`', '').strip()
    if line == '## Next Planned Feature' and index + 2 < len(text):
        next_planned = text[index + 2].removeprefix('- ').replace('`', '').strip()
print(f'{active}|{next_planned}')
PY2
)"
IFS='|' read -r handoff_active handoff_next <<< "$handoff_state"

if [[ "$in_progress_count" -eq 1 ]]; then
  active_id="$active_ids_csv"
  if [[ -z "$progress_id" ]]; then
    echo "[FAIL] feature_list.json shows active feature $active_id but PROGRESS.MD has no active feature ID"
    issues=$((issues + 1))
  elif [[ "$progress_id" != "$active_id" ]]; then
    echo "[FAIL] Active feature mismatch: feature_list.json=$active_id, PROGRESS.MD=$progress_id"
    issues=$((issues + 1))
  else
    echo "[OK] PROGRESS.MD active feature matches feature_list.json: $active_id"
  fi

  if [[ "$handoff_active" != "$active_id" && "$handoff_active" != "$active_id "* ]]; then
    echo "[FAIL] SESSION_HANDOFF.MD active feature does not match feature_list.json: expected $active_id, found '$handoff_active'"
    issues=$((issues + 1))
  else
    echo "[OK] SESSION_HANDOFF.MD active feature matches feature_list.json: $active_id"
  fi
else
  if [[ -n "$progress_id" ]] || [[ -n "$progress_status" && "$progress_status" != "planned | in_progress | blocked | done" ]]; then
    echo "[WARN] No active feature in feature_list.json, but PROGRESS.MD is not at the template state"
    warnings=$((warnings + 1))
  else
    echo "[OK] PROGRESS.MD is at template state with no active feature"
  fi

  handoff_active_normalized="$(printf "%s" "$handoff_active" | tr '[:upper:]' '[:lower:]')"
  if [[ -z "$handoff_active_normalized" || "$handoff_active_normalized" == "none" || "$handoff_active_normalized" == "none." || "$handoff_active_normalized" == "none currently active" || "$handoff_active_normalized" == "none currently active." ]]; then
    echo "[OK] SESSION_HANDOFF.MD reports no active feature"
  else
    echo "[FAIL] SESSION_HANDOFF.MD reports an active feature even though feature_list.json has none: '$handoff_active'"
    issues=$((issues + 1))
  fi

  if [[ -n "$next_planned_id" ]]; then
    if [[ "$handoff_next" == "$next_planned_id" || "$handoff_next" == "$next_planned_id "* ]]; then
      echo "[OK] SESSION_HANDOFF.MD next planned feature matches feature_list.json: $next_planned_id"
    else
      echo "[FAIL] Next planned feature mismatch: feature_list.json=$next_planned_id, SESSION_HANDOFF.MD='$handoff_next'"
      issues=$((issues + 1))
    fi
  fi
fi

base_url_state="$(python3 - <<'PY2'
from pathlib import Path

lines = Path('app/build.gradle.kts').read_text().splitlines()
current = None
debug = ''
release = ''
for raw_line in lines:
    stripped = raw_line.strip()
    if stripped == 'debug {':
        current = 'debug'
        continue
    if stripped == 'release {':
        current = 'release'
        continue
    if current and stripped.startswith('buildConfigField("String", "BASE_URL"'):
        parts = stripped.split('\\"')
        value = parts[1] if len(parts) > 1 else ''
        if current == 'debug':
            debug = value
        elif current == 'release':
            release = value
    if current and stripped == '}':
        current = None
print(f'{debug}|{release}')
PY2
)"
IFS='|' read -r debug_base_url release_base_url <<< "$base_url_state"

if [[ "$debug_base_url" == "http://10.0.2.2:5015/" ]]; then
  echo "[OK] Debug BASE_URL matches emulator local backend target"
else
  echo "[FAIL] Debug BASE_URL mismatch: expected http://10.0.2.2:5015/, found '$debug_base_url'"
  issues=$((issues + 1))
fi

if [[ "$release_base_url" == https://* && "$release_base_url" != *"10.0.2.2"* && "$release_base_url" != *"localhost"* ]]; then
  echo "[OK] Release BASE_URL stays off local cleartext hosts"
else
  echo "[FAIL] Release BASE_URL should be non-local HTTPS, found '$release_base_url'"
  issues=$((issues + 1))
fi

if command -v rg >/dev/null 2>&1; then
  manifest_check_cmd=(rg -q 'networkSecurityConfig="@xml/debug_network_security_config"' app/src/debug/AndroidManifest.xml)
else
  manifest_check_cmd=(grep -q 'networkSecurityConfig="@xml/debug_network_security_config"' app/src/debug/AndroidManifest.xml)
fi

if "${manifest_check_cmd[@]}"; then
  echo "[OK] Debug manifest points to debug network security config"
else
  echo "[FAIL] Debug manifest is missing the debug network security config reference"
  issues=$((issues + 1))
fi

local_domains="$(python3 - <<'PY2'
from pathlib import Path
import re

text = Path('app/src/debug/res/xml/debug_network_security_config.xml').read_text()
domains = re.findall(r'<domain[^>]*>([^<]+)</domain>', text)
print(','.join(domains))
PY2
)"
if [[ "$local_domains" == "10.0.2.2,localhost" || "$local_domains" == "localhost,10.0.2.2" ]]; then
  echo "[OK] Debug cleartext config is limited to approved local hosts"
else
  echo "[FAIL] Debug cleartext domains should be only 10.0.2.2 and localhost, found '$local_domains'"
  issues=$((issues + 1))
fi

session_check_reason=""
if contains_csv "$active_ids_csv" "ST-03"; then
  session_check_reason="active sign-in scope"
elif [[ "$next_planned_id" == "ST-03" ]]; then
  session_check_reason="next planned sign-in scope"
elif contains_csv "$done_ids_csv" "ST-03"; then
  session_check_reason="completed sign-in scope"
elif contains_csv "$active_dependency_ids_csv" "ST-03" || contains_csv "$next_dependency_ids_csv" "ST-03"; then
  session_check_reason="current auth dependency on sign-in"
fi

if [[ -n "$session_check_reason" ]]; then
  session_artifact_count="$( (rg -l 'DataStore|accessToken|tokenType|expiresAt|UserSession|Session' core/storage domain/auth data/auth feature/auth --glob '*.kt' --glob '*.kts' 2>/dev/null || true) | wc -l | tr -d ' ')"
  if [[ "$session_artifact_count" -gt 0 ]]; then
    echo "[OK] Session-related auth artifacts detected for $session_check_reason ($session_artifact_count files)"
  else
    echo "[FAIL] ST-03/session-dependent scope requires session/token persistence artifacts, but none were detected"
    issues=$((issues + 1))
  fi
else
  echo "[INFO] Session/token persistence checks skipped: current scope does not require them yet"
fi

runtime_outputs=()
while IFS= read -r file; do
  runtime_outputs+=("$file")
done < <(read_manifest_files "runtime_agent_outputs")

stale_outputs=()
for output in "${runtime_outputs[@]}"; do
  if [[ -f "$output" && -n "$(tr -d '[:space:]' < "$output")" ]]; then
    stale_outputs+=("$output")
  fi
done

if [[ "${#stale_outputs[@]}" -eq 0 ]]; then
  echo "[OK] No stale runtime handoff files"
else
  echo "[WARN] Non-empty runtime handoff files detected:"
  for output in "${stale_outputs[@]}"; do
    echo "  - $output"
  done
  warnings=$((warnings + 1))
fi

if [[ "$issues" -eq 0 ]]; then
  echo "=== Result: CLEAN (${warnings} warning(s)) ==="
else
  echo "=== Result: $issues blocking issue(s), ${warnings} warning(s) ==="
  exit 1
fi
