# VERIFICATION.MD — ZY-Commerce Android

This file is the operational verification guide for the Android repository. Use it to decide what to run, what evidence to record, and what to do when checks fail. Use `.harness/docs/TESTING_STRATEGY.MD` for test coverage expectations.

## Standard Path

Use the repository bootstrap script for normal verification:

```bash
./init.sh
```

`init.sh` confirms the repository root, selects the local JDK 17 path when possible, prints active feature status, and runs the Gradle checks below.

## Gradle Checks

The standard Android verification pack is:

```bash
./gradlew test
./gradlew lintDebug
./gradlew assembleDebug
```

On the current local machine, use JDK 17 explicitly if the shell does not expose a compatible `JAVA_HOME`:

```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew test
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew lintDebug
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew assembleDebug
```

## Feature Completion Evidence

A feature is not done until verification evidence is recorded in `feature_list.json` and the active feature notes in `.harness/docs/PROGRESS.MD`.

When the feature is complete, move the finished progress summary to the top of `.harness/docs/HISTORY.MD` and reset `.harness/docs/PROGRESS.MD` to the active-feature template.

For Android features, record:

- exact commands run
- pass/fail outcome
- notable warnings or skipped checks
- relevant test coverage added or updated
- manual smoke results when user-facing behavior is involved
- known gaps that remain out of scope

## Verification Layers

Use three layers where applicable:

1. Syntax / static checks: compilation, Gradle configuration, and Android lint.
2. Behavior checks: the relevant unit, integration, or UI tests from `.harness/docs/TESTING_STRATEGY.MD`.
3. System checks: backend connectivity and emulator/device smoke testing when user-facing or backend-dependent behavior changes.

Do not skip from code-written to done without evidence.

## Backend-Dependent Checks

Backend availability checks, startup commands, local URLs, and Android connection rules live in `.harness/docs/BACKEND_LOCAL.MD`. Run those checks before manual backend or emulator smoke testing.

## Current Manual Smoke Expectations

Current backend-dependent smoke prerequisites and `ST-01` registration expectations live in `.harness/docs/BACKEND_LOCAL.MD`. Record results here only when the expected verification shape changes.

## Failure Reporting

When a check fails, record actionable repair context.

Prefer:

```text
Login screen UI test failed: sign-in success did not navigate to catalog. Check session persistence and navigation effect handling in LoginViewModel and sign-in route.
```

Avoid:

```text
Test failed.
```
