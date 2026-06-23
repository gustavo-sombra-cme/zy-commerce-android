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
- `requirements/mvp_requirements_ecommerce.md`
- `requirements/ecommerce-android-plan.md`
- `requirements/ecommerce-android-tech-design.md`
- `requirements/zy-commerce-backend-business-baseline.md`
- `feature_list.json`
- `docs/SETUP.MD`
- `docs/PROGRESS.MD`
- `docs/SESSION_HANDOFF.MD`
- `docs/DECISIONS.MD`
- `docs/VERIFICATION.MD`
- `docs/TESTING_STRATEGY.MD`
- `docs/BACKEND_LOCAL.MD`
- `docs/backend-installation-instructions.md`
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

Use `./init.sh` for the standard local verification path. Test coverage expectations live in `docs/TESTING_STRATEGY.MD`; command details, evidence format, and optional smoke checks live in `docs/VERIFICATION.MD`.

## Backend Dependency

The Android app depends on the local ZY-Commerce backend for the current MVP. Backend startup, URLs, emulator/device connection rules, and debug network security details live in `docs/BACKEND_LOCAL.MD`.

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
