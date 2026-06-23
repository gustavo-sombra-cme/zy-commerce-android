# ZY-Commerce Backend macOS Installation and Run Instructions

## Brief Summary

This setup prepares the ZY-Commerce backend to build and run on macOS Apple Silicon using Homebrew as the primary installation path.

What was done:

- Installed Homebrew-managed .NET 9 SDK (`dotnet@9`).
- Installed Entity Framework Core CLI (`dotnet-ef`) as a .NET global tool because Homebrew does not provide a `dotnet-ef` formula.
- Installed Docker CLI, Colima, QEMU, Lima guest agents, and `sqlcmd`.
- Started Colima as an `x86_64` Docker VM so SQL Server can run on Apple Silicon.
- Started a SQL Server 2022 container.
- Applied the existing EF Core migrations for:
  - `EcommerceAuth`
  - `EcommerceCatalog`
- Built and tested the full solution.
- Started the API with macOS-compatible SQL Server connection strings.

Verified result:

- API: `http://localhost:5015`
- Swagger: `http://localhost:5015/swagger/index.html`
- Catalog endpoint: `GET http://localhost:5015/api/catalog/products`

## macOS Blockers and Challenges

The project was originally configured for SQL Server LocalDB:

```json
"Server=(localdb)\\mssqllocaldb"
```

LocalDB is Windows-only. On macOS this fails with:

```text
LocalDB is not supported on this platform.
```

To run on macOS, use a real SQL Server instance instead. In this setup, SQL Server runs in Docker through Colima.

Other challenges encountered:

- `dotnet@9` is keg-only in Homebrew, so `dotnet` is not automatically linked into `/opt/homebrew/bin`.
- The shell profile needed `DOTNET_ROOT` and PATH entries for `.NET` and `.NET global tools`.
- Homebrew does not provide `dotnet-ef`; it must be installed with `dotnet tool install`.
- Apple Silicon cannot run the SQL Server Linux container natively as `arm64`, so Colima must run an `x86_64` VM.
- Colima required QEMU for `x86_64` emulation.
- Colima required `lima-additional-guestagents` for the `x86_64` VM.
- The Colima VM user initially was not in the `docker` group, causing Docker socket permission issues. Adding the `lima` user to the `docker` group and restarting Colima fixed it.
- SQL Server takes time on first boot while system databases are initialized and upgraded. Early `sqlcmd` attempts can fail until recovery completes.

## Step-by-Step Setup

These commands assume Apple Silicon macOS and a zsh shell.

Project path:

```sh
cd /Users/gustavo.sombra/Desktop/ZippyYum/AI/Projects/ZY-Commerce/zy-commerce-backend
```

### 1. Verify Homebrew

```sh
/opt/homebrew/bin/brew --version
```

If `brew` is not found in a normal terminal, add Homebrew to PATH:

```sh
echo 'export PATH="/opt/homebrew/bin:$PATH"' >> ~/.zprofile
exec zsh -l
```

### 2. Install .NET 9 SDK With Homebrew

```sh
brew install dotnet@9
```

Because `dotnet@9` is keg-only, add it to your zsh profile:

```sh
cat <<'EOF' >> ~/.zprofile

# ZY-Commerce .NET 9 SDK setup
export DOTNET_ROOT="/opt/homebrew/opt/dotnet@9/libexec"
export PATH="$DOTNET_ROOT:/Users/gustavo.sombra/.dotnet/tools:/opt/homebrew/bin:$PATH"
EOF

exec zsh -l
```

Verify:

```sh
dotnet --info
dotnet --version
```

Expected SDK version from this setup:

```text
9.0.117
```

### 3. Restore, Build, and Test

```sh
cd /Users/gustavo.sombra/Desktop/ZippyYum/AI/Projects/ZY-Commerce/zy-commerce-backend

dotnet restore Ecommerce.sln
dotnet build Ecommerce.sln --no-restore
dotnet test Ecommerce.sln --no-build
```

Expected test result from this setup:

```text
150 tests passed
```

### 4. Install EF Core CLI

Homebrew does not provide `dotnet-ef`, so install it as a .NET global tool:

```sh
dotnet tool install --global dotnet-ef --version '9.*'
```

If it is already installed, update it instead:

```sh
dotnet tool update --global dotnet-ef --version '9.*'
```

Verify:

```sh
dotnet-ef --version
```

Expected version from this setup:

```text
9.0.17
```

### 5. Install Docker, Colima, SQL Tools, and Emulation Dependencies

```sh
brew install docker colima sqlcmd qemu lima-additional-guestagents
```

### 6. Start Colima as x86_64

SQL Server requires an `amd64`/`x86_64` Linux environment, so start Colima with `x86_64` architecture:

```sh
colima start --arch x86_64 --memory 4 --cpu 2 --disk 20
```

If Docker socket permissions fail inside Colima, run:

```sh
colima ssh -- sudo usermod -aG docker lima
colima stop
colima start
```

Verify Colima and Docker:

```sh
colima status
docker version
```

Expected Docker server architecture:

```text
linux/amd64
```

### 7. Start SQL Server Container

Use a development password that satisfies SQL Server password requirements:

```sh
docker run \
  --name zy-commerce-sql \
  -e ACCEPT_EULA=Y \
  -e MSSQL_SA_PASSWORD=ZippyYum_DevPass123 \
  -p 1433:1433 \
  -d mcr.microsoft.com/mssql/server:2022-latest
```

If the container already exists, start it:

```sh
docker start zy-commerce-sql
```

Check status:

```sh
docker ps --filter name=zy-commerce-sql
```

Watch logs until recovery completes:

```sh
docker logs --tail 120 zy-commerce-sql
```

Verify SQL Server login:

```sh
sqlcmd -S localhost,1433 -U sa -P ZippyYum_DevPass123 -C -Q 'SELECT name FROM sys.databases ORDER BY name'
```

### 8. Apply EF Core Migrations

The checked-in appsettings use Windows LocalDB, so override connection strings with environment variables.

Apply Auth migration:

```sh
ConnectionStrings__Auth='Server=localhost,1433;Database=EcommerceAuth;User Id=sa;Password=ZippyYum_DevPass123;TrustServerCertificate=True;Encrypt=True' \
dotnet-ef database update \
  --project src/Modules/Auth/Ecommerce.Auth.Infrastructure/Ecommerce.Auth.Infrastructure.csproj \
  --startup-project src/Api/Ecommerce.Api/Ecommerce.Api.csproj \
  --context AuthDbContext
```

Apply Catalog migration:

```sh
ConnectionStrings__Catalog='Server=localhost,1433;Database=EcommerceCatalog;User Id=sa;Password=ZippyYum_DevPass123;TrustServerCertificate=True;Encrypt=True' \
dotnet-ef database update \
  --project src/Modules/Catalog/Ecommerce.Catalog.Infrastructure/Ecommerce.Catalog.Infrastructure.csproj \
  --startup-project src/Api/Ecommerce.Api/Ecommerce.Api.csproj \
  --context CatalogDbContext
```

Verify databases:

```sh
sqlcmd -S localhost,1433 -U sa -P ZippyYum_DevPass123 -C -Q 'SELECT name FROM sys.databases ORDER BY name'
```

Expected project databases:

```text
EcommerceAuth
EcommerceCatalog
```

### 9. Start the API Server

Start the API with SQL Server container connection strings:

```sh
ASPNETCORE_ENVIRONMENT=Development \
ConnectionStrings__Auth='Server=localhost,1433;Database=EcommerceAuth;User Id=sa;Password=ZippyYum_DevPass123;TrustServerCertificate=True;Encrypt=True' \
ConnectionStrings__Catalog='Server=localhost,1433;Database=EcommerceCatalog;User Id=sa;Password=ZippyYum_DevPass123;TrustServerCertificate=True;Encrypt=True' \
dotnet run \
  --project src/Api/Ecommerce.Api/Ecommerce.Api.csproj \
  --launch-profile http
```

The API should listen on:

```text
http://localhost:5015
```

Swagger:

```text
http://localhost:5015/swagger/index.html
```

### 10. Verify the Running API

```sh
curl -I http://localhost:5015/swagger/index.html
curl -i http://localhost:5015/api/catalog/products
```

Expected results:

- Swagger returns `HTTP/1.1 200 OK`.
- Catalog products returns `HTTP/1.1 200 OK`.

For a fresh database, the Catalog response should look similar to:

```json
{"items":[],"pageNumber":1,"pageSize":20,"totalCount":0,"totalPages":0,"hasPreviousPage":false,"hasNextPage":false}
```

## Daily Run Commands

After the first-time setup is complete, use this shorter flow.

```sh
cd /Users/gustavo.sombra/Desktop/ZippyYum/AI/Projects/ZY-Commerce/zy-commerce-backend

colima start
docker start zy-commerce-sql

ASPNETCORE_ENVIRONMENT=Development \
ConnectionStrings__Auth='Server=localhost,1433;Database=EcommerceAuth;User Id=sa;Password=ZippyYum_DevPass123;TrustServerCertificate=True;Encrypt=True' \
ConnectionStrings__Catalog='Server=localhost,1433;Database=EcommerceCatalog;User Id=sa;Password=ZippyYum_DevPass123;TrustServerCertificate=True;Encrypt=True' \
dotnet run \
  --project src/Api/Ecommerce.Api/Ecommerce.Api.csproj \
  --launch-profile http
```

## Useful Troubleshooting Commands

Check .NET:

```sh
dotnet --info
dotnet-ef --version
```

Check Colima:

```sh
colima status
colima stop
colima start
```

Check Docker:

```sh
docker version
docker ps -a
docker logs --tail 120 zy-commerce-sql
```

Check SQL Server:

```sh
sqlcmd -S localhost,1433 -U sa -P ZippyYum_DevPass123 -C -Q 'SELECT @@VERSION'
sqlcmd -S localhost,1433 -U sa -P ZippyYum_DevPass123 -C -Q 'SELECT name FROM sys.databases ORDER BY name'
```

If port `5015` is already in use:

```sh
lsof -nP -iTCP:5015 -sTCP:LISTEN
```

Then stop the relevant API process if needed:

```sh
kill <PID>
```

