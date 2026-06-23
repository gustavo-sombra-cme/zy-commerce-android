#!/usr/bin/env bash
set -euo pipefail

export PATH="/opt/homebrew/bin:$PATH"

if ! colima status >/dev/null 2>&1; then
  colima start
else
  echo "Colima is already running."
fi

if docker ps --format '{{.Names}}' | grep -qx 'zy-commerce-sql'; then
  echo "zy-commerce-sql is already running."
elif docker ps -a --format '{{.Names}}' | grep -qx 'zy-commerce-sql'; then
  docker start zy-commerce-sql
else
  docker run \
    --name zy-commerce-sql \
    -e ACCEPT_EULA=Y \
    -e MSSQL_SA_PASSWORD=ZippyYum_DevPass123 \
    -p 1433:1433 \
    -d mcr.microsoft.com/mssql/server:2022-latest
fi
