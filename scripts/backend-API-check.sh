#!/usr/bin/env bash
set -euo pipefail

if lsof -nP -iTCP:5015 -sTCP:LISTEN; then
  echo "Backend API is running on http://localhost:5015"
else
  echo "Backend API is not running on port 5015."
fi
