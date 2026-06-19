# SETUP.MD — ZY-Commerce Android

## Purpose

This document defines how to bootstrap the ZY-Commerce Android project safely at its current stage.

Important current reality:

- the repository does **not** yet contain an Android app scaffold
- there is no Gradle wrapper yet
- there is no executable `init.sh` yet
- the backend already exists and can run locally on macOS

Because of that, this document serves as the current startup and environment guide. Once the Android project scaffold exists, it should be complemented or partially replaced by a real `init.sh`.

## Current Repository State

What exists now:

- `AGENTS.MD`
- `docs/PRODUCT.MD`
- `docs/ARCHITECTURE.MD`
- `docs/mvp_requirements_ecommerce.md`
- `docs/ecommerce-android-plan.md`
- `feature_list.json`
- `docs/PROGRESS.MD`
- `docs/SESSION_HANDOFF.MD`

What does not exist yet:

- Android Studio project structure
- Gradle wrapper
- app module
- feature modules
- tests
- CI configuration
- executable bootstrap script

## Bootstrap Goals

Before feature development begins, the environment should support:

1. reading the repository context quickly
2. confirming the backend is available
3. knowing how the Android client will connect to the backend
4. scaffolding the Android project in alignment with `docs/ARCHITECTURE.MD`
5. later running a standard verification path consistently

## Required Reading Before Work

Before making changes, read in this order:

1. `AGENTS.MD`
2. `docs/PRODUCT.MD`
3. `docs/ARCHITECTURE.MD`
4. `feature_list.json`
5. `docs/ecommerce-android-plan.md`
6. `docs/PROGRESS.MD`
7. `docs/SESSION_HANDOFF.MD`

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

Before Android integration work, confirm the backend is available.

If the backend repository and supporting scripts are present:

```bash
/Users/gustavo.sombra/Desktop/ZippyYum/AI/Projects/ZY-Commerce/backend-API-check.sh
```

If the API is not running, start it using the backend startup path already defined in the parent project:

```bash
/Users/gustavo.sombra/Desktop/ZippyYum/AI/Projects/ZY-Commerce/backend-startup.sh
```

## Android-to-Backend Connection Rules

Use environment-appropriate base URLs:

### Android Emulator

Use:

```text
http://10.0.2.2:5015/
```

Why:

- `10.0.2.2` is the Android emulator alias for the host machine’s localhost

### Physical Device On Same Network

Use:

```text
http://<mac-local-ip>:5015/
```

Requirements:

- Mac and device must be on the same Wi-Fi network
- backend must be listening and reachable from LAN

### Release / Non-Local Environments

Do not assume local HTTP.

Use environment-specific configuration and HTTPS when non-local environments exist.

## Network Security Rules

Because the local backend currently uses HTTP, the Android project will need:

- debug-only cleartext allowance
- no broad cleartext allowance in release builds
- explicit environment separation for emulator, LAN device, and future staging/prod

This setup must align with `docs/ARCHITECTURE.MD`.

## What To Do Before Scaffolding The Android App

1. Confirm the docs are the current source of truth.
2. Confirm the backend API is running or can be started.
3. Confirm the next feature to implement from `feature_list.json`.
4. Confirm the Android scaffold will follow the module structure in `docs/ARCHITECTURE.MD`.

## Recommended First Scaffold

When implementation starts, scaffold at minimum:

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

Initial priorities:

- app shell
- navigation host
- DI setup
- network layer
- session storage

Do not start by implementing product-management screens before the shell, network, and auth/session foundations exist.

## Planned Verification Path After Scaffold Exists

Once Gradle exists, the standard baseline should become:

```bash
./gradlew test
./gradlew lintDebug
./gradlew assembleDebug
```

Add these as they become available:

- Compose UI test tasks
- repository/integration test tasks
- screenshot regression tasks

## Planned `init.sh` Contract

When the project is scaffolded, `init.sh` should:

1. confirm working directory
2. confirm backend availability
3. confirm required local tools exist
4. run the standard Gradle verification path
5. print the current active feature and any failing checks

At that point, `AGENTS.MD` should be updated so `init.sh` is the standard startup path rather than this document alone.

## Tooling Expectations For Future Scaffold

When the Android app is created, the environment should support:

- Android Studio
- JDK compatible with the chosen Android Gradle Plugin
- Gradle wrapper checked into the repo
- emulator or physical device testing path

Do not hardcode future AGP, Kotlin, or Compose versions in this document unless they are committed in the project build files.

## Manual Startup Checklist For Now

Use this until a real bootstrap script exists:

1. Read the repository docs in the required order.
2. Confirm the backend is running.
3. Confirm the next feature from `feature_list.json`.
4. Confirm no baseline doc inconsistency exists.
5. Only then begin scaffolding or implementation work.

## Definition Of A Healthy Startup State

The project is in a healthy startup state when:

- docs are internally consistent
- backend is reachable or startup path is known
- current active feature is clear
- architecture constraints are known
- there is a defined next action

## Next Upgrade After This File

The next bootstrap improvement should be:

1. scaffold the Android project
2. add Gradle wrapper
3. create `init.sh`
4. update `AGENTS.MD` to call `init.sh` directly
