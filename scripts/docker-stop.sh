#!/usr/bin/env bash
set -euo pipefail

export PATH="/opt/homebrew/bin:$PATH"

if docker ps --format '{{.Names}}' | grep -qx 'zy-commerce-sql'; then
  docker stop zy-commerce-sql
else
  echo "zy-commerce-sql is not running."
fi

if colima status >/dev/null 2>&1; then
  colima stop
else
  echo "Colima is not running."
fi
