#!/usr/bin/env bash
# .harness/verify.sh — Harness-controlled feature verifier
# Only this script may transition features to "passing" state.
set -euo pipefail

FEATURE_LIST="docs/feature_list.json"
command -v jq >/dev/null 2>&1 || { echo "jq required"; exit 1; }

ACTIVE_ID=$(jq -r '.features[] | select(.state == "active") | .id' "$FEATURE_LIST")
if [ -z "$ACTIVE_ID" ]; then echo "No active feature."; exit 0; fi

TEST_CMD=$(jq -r --arg id "$ACTIVE_ID" '.features[] | select(.id == $id) | .test_command' "$FEATURE_LIST")
echo "▶ Verifying $ACTIVE_ID: $TEST_CMD"

if eval "$TEST_CMD"; then
  echo "✅ $ACTIVE_ID tests passed — transitioning to passing"
  UPDATED=$(jq --arg id "$ACTIVE_ID" \
    '(.features[] | select(.id == $id) | .state) = "passing"' \
    "$FEATURE_LIST")
  echo "$UPDATED" > "$FEATURE_LIST"
  echo "feature_list.json updated."
else
  echo "❌ $ACTIVE_ID tests FAILED — state unchanged"
  exit 1
fi
