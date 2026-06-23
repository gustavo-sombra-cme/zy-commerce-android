#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="/Users/gustavo.sombra/Desktop/ZippyYum/AI/Projects/ZY-Commerce/zy-commerce-backend"

export DOTNET_ROOT="/opt/homebrew/opt/dotnet@9/libexec"
export PATH="$DOTNET_ROOT:/Users/gustavo.sombra/.dotnet/tools:/opt/homebrew/bin:$PATH"

"$SCRIPT_DIR/docker-start.sh"

cd "$PROJECT_DIR"

ASPNETCORE_ENVIRONMENT=Development \
ConnectionStrings__Auth='Server=localhost,1433;Database=EcommerceAuth;User Id=sa;Password=ZippyYum_DevPass123;TrustServerCertificate=True;Encrypt=True' \
ConnectionStrings__Catalog='Server=localhost,1433;Database=EcommerceCatalog;User Id=sa;Password=ZippyYum_DevPass123;TrustServerCertificate=True;Encrypt=True' \
dotnet run --project src/Api/Ecommerce.Api/Ecommerce.Api.csproj --launch-profile http
