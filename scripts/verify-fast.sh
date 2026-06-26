#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== Fast Startup Verification ==="

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
done < <(read_manifest_files "fast_required_files")

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
  echo "Fast verification failed because required startup-surface files are missing." >&2
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

echo "Running cleanup scanner..."
bash scripts/cleanup-scanner.sh

echo "=== Fast startup verification passed. ==="
