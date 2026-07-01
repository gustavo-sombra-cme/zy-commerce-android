# VERIFICATION.MD — ZY-Commerce Android

This file is the operational verification guide for the Android repository. Use it to decide what to run, what evidence to record, and what to do when checks fail. Use `.harness/docs/TESTING_STRATEGY.MD` for test coverage expectations and `docs/RELIABILITY.MD` for lifecycle and runtime expectations.

## Standard Path

Use the repository bootstrap script for normal verification:

```bash
./init.sh
```

`init.sh` confirms the repository root, validates required harness files, selects the local JDK 17 path when possible, prints the active or next feature, runs the Gradle checks below, and executes the cleanup scanner.

## Fast Path

Use this when you need a quick startup-surface check before editing or before a longer full verification run:

```bash
bash scripts/verify-fast.sh
```

`scripts/verify-fast.sh` is intentionally lighter than `init.sh`. It checks the startup surface, confirms feature-state readability, and runs the cleanup scanner without running the full Gradle suite.

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

After an Evaluator `pass` decision and explicit human approval, close the feature through `scripts/close-cycle.sh --approved ...`. The closure script owns the repeated state updates: durable feature evidence, history archive, progress reset, handoff rewrite, and runtime output cleanup.

For Android features, record:

- exact commands run
- pass or fail outcome
- notable warnings or skipped checks
- relevant test coverage added or updated
- manual smoke results when user-facing behavior is involved
- known gaps that remain out of scope

## Verification Layers

Use three layers where applicable:

1. Syntax or static checks: compilation, Gradle configuration, and Android lint.
2. Behavior checks: the relevant unit, integration, or UI tests from `.harness/docs/TESTING_STRATEGY.MD`.
3. System checks: backend connectivity and emulator or device smoke testing when user-facing or backend-dependent behavior changes.

Do not skip from code-written to done without evidence.

## Backend-Dependent Checks

Backend availability checks, startup commands, local URLs, and Android connection rules live in `.harness/docs/BACKEND_LOCAL.MD`. Run those checks before manual backend or emulator smoke testing.

## Lifecycle Checks

Use these additional lifecycle checks when harness maturity or restartability changed:

```bash
bash scripts/cleanup-scanner.sh
bash scripts/harness-benchmark.sh
```

The benchmark should stay lightweight. It may run a cheap backend-availability check, local-network sanity checks, and a focused targeted verification command for the active or next feature when that mapping exists.

Use `.harness/docs/clean-state-checklist.md` before finishing risky sessions or harness refactors.

## Current Manual Smoke Expectations

Backend-dependent smoke prerequisites live in `.harness/docs/BACKEND_LOCAL.MD`. Story-specific smoke expectations belong in `feature_list.json` and the active notes in `.harness/docs/PROGRESS.MD`; for auth and profile work, include backend reachability, the endpoint exercised, session state, device target, and any skipped paths. Use `docs/RELIABILITY.MD` for what reliable smoke evidence should contain. Record results here only when the expected verification shape changes.

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
