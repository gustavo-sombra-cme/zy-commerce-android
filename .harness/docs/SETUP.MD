# SETUP.MD — ZY-Commerce Android

## Purpose

This document defines how to bootstrap the ZY-Commerce Android project safely on a local machine.

## Standard Startup Path

Use the repository bootstrap script:

```bash
./init.sh
```

`init.sh` is the canonical startup path. It checks required harness files, selects the local JDK 17 path when possible, prints the current active or next feature, and runs the standard Gradle verification pack.

For a lightweight startup-surface check before a longer verification run, use:

```bash
bash scripts/verify-fast.sh
```

## Required Local Tooling

The currently verified local path uses:

- Android SDK at `/Users/gustavo.sombra/Library/Android/sdk`
- JDK 17
- Gradle wrapper distribution `8.11.1`
- Ruby with the standard `json` library for the startup feature summary in `init.sh`

If `JAVA_HOME` is not set, `init.sh` will try:

```text
/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
```

## Repository Shape Expected At Startup

The startup path expects these categories to be present and aligned:

- product and architecture docs in `docs/`
- source requirements in `requirements/`
- feature state in `feature_list.json`
- harness operational docs in `.harness/docs/`
- role prompts in `.harness/agents/`
- Gradle wrapper and Android modules in the repository root

Use `AGENTS.MD` for the operating contract, not this file.

## Module Layout

Current Android module layout:

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

## Related Docs

- `AGENTS.MD` — startup order, scope rules, and completion rules
- `.harness/docs/BACKEND_LOCAL.MD` — backend startup and connection path
- `.harness/docs/TESTING_STRATEGY.MD` — test coverage expectations
- `.harness/docs/VERIFICATION.MD` — commands, evidence format, and failure reporting
- `.harness/docs/clean-state-checklist.md` — end-of-session and pre-commit clean-state checks
- `docs/RELIABILITY.MD` — lifecycle, benchmark, and cleanup expectations

## Healthy Startup State

The repository is in a healthy startup state when:

- required harness files exist
- docs are internally consistent
- the active feature is clear from `feature_list.json`, `.harness/docs/PROGRESS.MD`, and `.harness/docs/SESSION_HANDOFF.MD`
- `./init.sh` passes
- the next session can continue without reading prior chat history
