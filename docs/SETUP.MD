# SETUP.MD — ZY-Commerce Android

## Purpose

This document defines how to bootstrap the ZY-Commerce Android project safely at its current stage.

Current reality as of 2026-06-19:

- the repository now contains an Android Gradle scaffold
- the Gradle wrapper is checked in
- the `app` module exists and launches the registration flow
- `init.sh` is the standard local verification entry point
- the backend already exists and can run locally on macOS

## Current Repository State

What exists now:

- `AGENTS.MD`
- `docs/PRODUCT.MD`
- `docs/ARCHITECTURE.MD`
- `docs/mvp_requirements_ecommerce.md`
- `docs/ecommerce-android-plan.md`
- `feature_list.json`
- `docs/SETUP.MD`
- `docs/PROGRESS.MD`
- `docs/SESSION_HANDOFF.MD`
- Android Gradle scaffold with modules aligned to `docs/ARCHITECTURE.MD`
- Gradle wrapper: `./gradlew`
- bootstrap script: `./init.sh`
- implemented `ST-01` registration flow

Current module scaffold:

```text
app/
core/common/
core/designsystem/
core/network/
core/database/
core/storage/
domain/auth/
domain/catalog/
data/auth/
data/catalog/
feature/auth/
feature/catalog/
feature/productadmin/
```

## Required Local Tooling

The verified local path uses:

- Android SDK at `/Users/gustavo.sombra/Library/Android/sdk`
- JDK 17
- Gradle wrapper distribution `8.11.1`

If `JAVA_HOME` is not set, `init.sh` will use:

```text
/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
```

## Standard Verification Path

Use:

```bash
./init.sh
```

`init.sh` runs:

```bash
./gradlew test
./gradlew lintDebug
./gradlew assembleDebug
```

You can also run the Gradle commands directly. On this machine, use JDK 17 explicitly if the shell does not already expose it:

```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew test
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew lintDebug
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew assembleDebug
```

## Backend Dependency

The Android app depends on the local ZY-Commerce backend for the current MVP.

Backend-related project files available outside this repo:

- `backend-installation-instructions.md`
- `backend-startup.sh`
- `backend-API-check.sh`

Current backend runtime target:

- API: `http://localhost:5015`
- Swagger: `http://localhost:5015/swagger/index.html`

## Backend Startup Check

Before Android integration or manual registration testing, confirm the backend is available.

If the backend repository and supporting scripts are present:

```bash
/Users/gustavo.sombra/Desktop/ZippyYum/AI/Projects/ZY-Commerce/backend-API-check.sh
```

If the API is not running, start it using the backend startup path already defined in the parent project:

```bash
/Users/gustavo.sombra/Desktop/ZippyYum/AI/Projects/ZY-Commerce/backend-startup.sh
```

## Android-to-Backend Connection Rules

Debug emulator builds use:

```text
http://10.0.2.2:5015/
```

Why:

- `10.0.2.2` is the Android emulator alias for the host machine's localhost

Physical device builds on the same network should use:

```text
http://<mac-local-ip>:5015/
```

Release / non-local environments must not assume local HTTP. Use environment-specific HTTPS configuration when those environments exist.

## Network Security Rules

The debug manifest allows cleartext traffic to local development hosts only:

- `10.0.2.2`
- `localhost`

Do not add broad release cleartext traffic.

## Current Feature State

`ST-01` Register account is implemented and verified.

Current `ST-01` behavior:

- visitor enters email and password on `RegisterScreen`
- registration posts to `POST /api/auth/users/register`
- response maps `userId` and `email` into the auth domain model
- success shows confirmation and navigates to the sign-in route placeholder

Out of current scope:

- duplicate email-specific UX (`ST-02`)
- actual sign-in implementation (`ST-03`)
- session persistence
- catalog and product-management behavior

## Definition Of A Healthy Startup State

The project is in a healthy startup state when:

- docs are internally consistent
- backend is reachable or startup path is known
- current active feature is clear
- architecture constraints are known
- `./init.sh` passes
