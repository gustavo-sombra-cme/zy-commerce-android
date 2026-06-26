#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== Fast Startup Verification ==="

required_files=(
  "AGENTS.MD"
  "docs/PRODUCT.MD"
  "docs/ARCHITECTURE.MD"
  "docs/RELIABILITY.MD"
  "feature_list.json"
  "evaluator-rubric.md"
  "quality-document.md"
  ".harness/docs/SETUP.MD"
  ".harness/docs/VERIFICATION.MD"
  ".harness/docs/TESTING_STRATEGY.MD"
  ".harness/docs/clean-state-checklist.md"
  "scripts/cleanup-scanner.sh"
)

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
