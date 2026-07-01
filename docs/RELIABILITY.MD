# RELIABILITY — ZY-Commerce Android

## Purpose

This document defines the Android repository's runtime reliability contract: what clean startup means, what clean finish means, which Android-specific states must stay inspectable, and what evidence should exist when a feature is claimed as complete.

Use this file for runtime and lifecycle expectations.
Use `.harness/docs/VERIFICATION.MD` for exact commands and evidence format.
Use `.harness/docs/TESTING_STRATEGY.MD` for test-level ownership and coverage depth.

## Reliability Goals

The harness should make every session restartable and every completion claim inspectable.

That means:

- startup succeeds from `./init.sh`
- feature status is readable without chat history
- verification evidence is written back to repository artifacts
- Android-specific state transitions are described before they are implemented
- cleanup checks detect harness drift and stale runtime assumptions
- backend-dependent smoke expectations are explicit, not implied

## Ownership Boundaries

This file owns:

- clean-state expectations
- Android runtime expectations for storage, tokens, networking, and protected routing
- benchmark intent
- cleanup intent
- what counts as reliable smoke evidence

This file does not own:

- exact commands to run
- per-layer test selection
- feature history
- active task notes

Those belong to:

- `.harness/docs/VERIFICATION.MD`
- `.harness/docs/TESTING_STRATEGY.MD`
- `.harness/docs/HISTORY.MD`
- `.harness/docs/PROGRESS.MD`

## Lifecycle Expectations

Before starting new feature work:

1. Read `AGENTS.MD`.
2. Run `./init.sh`.
3. Confirm the active or next feature is correct.
4. Use `.harness/docs/BACKEND_LOCAL.MD` if backend-dependent smoke checks are expected.
5. Re-check this document if the story touches session storage, local network configuration, or protected navigation.

Before ending a session:

1. Update `feature_list.json` if feature state or evidence changed.
2. Update `.harness/docs/PROGRESS.MD` if an active item exists.
3. Update `.harness/docs/HISTORY.MD` if work completed.
4. Update `.harness/docs/SESSION_HANDOFF.MD` for the next restart.
5. Run the clean-state checklist.
6. Record any skipped runtime expectations as skipped or not-yet-applicable, not silently passed.

## Clean State

Clean state in this repository means:

- no ambiguous active feature
- no stale role-output expectations left unresolved
- required harness docs exist and agree on ownership
- verification commands still reflect the current Gradle path
- backend-dependent work records whether smoke testing ran or was skipped
- runtime assumptions about Room, DataStore, tokens, and debug network config are still accurate

If a subsystem does not exist yet, clean state means the repository says so clearly. Do not pretend a reliability concern is solved just because the feature is not implemented yet.

## Android Runtime Expectations

### Room And Local Cache

Room is expected to act as a read cache, not a hidden source of truth for business writes.

Reliability expectations:

- cached product data must be safe to discard and rebuild from backend reads
- cache reset behavior must be explicit once Room entities and DAOs are implemented
- stale cached data should not mask failed backend refreshes during smoke testing
- when cache behavior changes, the verification evidence should say whether cache fallback or cache invalidation was exercised

### DataStore And Session State

DataStore-backed secure storage now holds session-oriented values for sign-in and splash restore.

Reliability expectations:

- access-token persistence rules must be documented before session restore is declared complete
- sign-out or invalid-session handling must clear persisted session state intentionally
- token expiration or invalid-token behavior must not leave the app in an ambiguous partially signed-in state
- auth and profile evidence should distinguish fresh sign-in, persisted restore, and token-failure behavior when those paths are relevant

### Tokens And Auth Boundaries

Auth state must remain inspectable and predictable.

Reliability expectations:

- protected requests only use auth state that is known to be current
- a `401` or equivalent invalid-session outcome should map to a deliberate session-clear or sign-in recovery path once session work exists
- verification evidence should distinguish happy-path login from token-restoration or token-failure behavior

### Debug Network Configuration

Debug builds may use local cleartext configuration only for approved local development hosts.

Reliability expectations:

- emulator and device base-URL assumptions match `.harness/docs/BACKEND_LOCAL.MD`
- local cleartext allowances stay debug-only
- release and non-local assumptions do not silently inherit local HTTP behavior
- if the base URL strategy changes, update `.harness/docs/BACKEND_LOCAL.MD` and verification notes in the same session

### Protected Action Routing

Visitors must be redirected or prompted clearly when a story requires authentication.

Reliability expectations:

- protected catalog or product-admin actions must not fail silently when signed out
- routing to sign-in or equivalent gating should be treated as user-visible behavior, not just internal state
- smoke evidence should say whether protected-action gating was exercised, skipped, or not yet applicable

## Smoke Evidence Expectations

Manual or scripted smoke evidence is reliable when it records:

- which device target was used: emulator, physical device, or not run
- whether the backend was running and reachable
- which base URL assumption was used
- which user-facing behavior was exercised
- what was intentionally skipped

Good examples:

- sign-in happy path verified on emulator against `10.0.2.2:5015`
- duplicate-email path covered by tests only; backend smoke skipped this session
- protected create-product action correctly routes signed-out user to sign-in prompt

Weak examples:

- smoke passed
- backend looked fine
- auth seems okay

## Benchmark Expectations

Use `bash scripts/harness-benchmark.sh` as a lightweight harness benchmark. It is not a runtime performance benchmark for the Android app itself. It is a repeatability check for the repo workflow and harness surface.

The current benchmark is intentionally light. It should stay selective and prefer:

- targeted feature-aware verification
- cheap backend-availability checks
- fast local-network sanity checks
- fast consistency checks for harness assumptions

Do not turn it into a slow full regression replacement.

## Cleanup Expectations

Use `bash scripts/cleanup-scanner.sh` to detect harness drift and stale execution state.

The cleanup path should help surface:

- missing required harness files
- conflicting active-feature state across `feature_list.json`, `PROGRESS.MD`, and `SESSION_HANDOFF.MD`
- stale role-output files
- local base-URL or debug-cleartext assumptions that drifted away from `.harness/docs/BACKEND_LOCAL.MD`
- outdated runtime assumptions once session, cache, and protected-routing features land

## Current Limitations

The repository does not yet have:

- automated emulator smoke testing
- automated backend contract replay
- end-to-end Android UI regression automation
- automated emulator or backend smoke coverage for persisted session restore and invalid-token recovery
- completed Room-backed catalog cache behavior

Until those exist, reliability depends on a combination of Gradle checks, targeted tests, backend availability checks, and explicit smoke or skip notes in `feature_list.json` or `.harness/docs/PROGRESS.MD`.
