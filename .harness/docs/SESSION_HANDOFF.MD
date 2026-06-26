# SESSION_HANDOFF.MD

This file is a short restart note for the next session only.

Keep it compact. Put durable completion history in `.harness/docs/HISTORY.MD` in newest-first order, active-item detail in `.harness/docs/PROGRESS.MD`, and feature state plus evidence in `feature_list.json`.

## Current Objective

- Implement `ST-03` sign in.

## Current Status

- Ready to start.

## Active Feature

- None currently active.

## Next Planned Feature

- `ST-03` Sign in

## What Changed Most Recently

- Harness ownership was tightened so startup docs, decisions, archive history, and lifecycle checks have sharper boundaries.
- `feature_list.json` remains the durable feature state and verification evidence source.
- `docs/RELIABILITY.MD`, `.harness/docs/clean-state-checklist.md`, `scripts/harness-benchmark.sh`, and `scripts/cleanup-scanner.sh` are now part of the startup surface.

## Verification Snapshot

- `./init.sh` passes.
- Current standard checks are `./gradlew test`, `./gradlew lintDebug`, and `./gradlew assembleDebug`.
- Lifecycle checks now also include `bash scripts/harness-benchmark.sh` and `bash scripts/cleanup-scanner.sh` when the feature touches restartability, backend wiring, or verification maturity.
- Manual backend/emulator smoke testing has not yet been rerun for the next feature.

## Blockers / Risks

- Local backend must be running before backend-dependent auth smoke checks.
- Sign-in API contract should be confirmed against the backend before finalizing UI and session behavior.

## Recommended Next Step

- Implement the `ST-03` sign-in flow across presentation, domain, and data layers, then add the matching verification evidence.

## Files Likely Needed Next

- `feature_list.json`
- `.harness/docs/PROGRESS.MD`
- `.harness/docs/BACKEND_LOCAL.MD`
- `.harness/docs/TESTING_STRATEGY.MD`
- `.harness/docs/VERIFICATION.MD`
- `docs/ARCHITECTURE.MD`
- `requirements/ecommerce-android-plan.md`
