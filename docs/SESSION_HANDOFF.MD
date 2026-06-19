# SESSION_HANDOFF.MD

## Resume Summary

The Android repository now has a usable first-pass harness surface and a documented bootstrap path.

Already in repo:

- `AGENTS.MD`
- `docs/PRODUCT.MD`
- `docs/ARCHITECTURE.MD`
- `docs/mvp_requirements_ecommerce.md`
- `docs/ecommerce-android-plan.md`
- `feature_list.json`
- `docs/SETUP.MD`
- `docs/PROGRESS.MD`
- `docs/SESSION_HANDOFF.MD`

## What Was Completed

- Created `AGENTS.MD` based on the Android docs and Harness Engineering guidance.
- Created continuity artifacts and bootstrap documentation:
  - `docs/PROGRESS.MD`
  - `docs/SESSION_HANDOFF.MD`
  - `docs/SETUP.MD`
- Updated the harness so `docs/SETUP.MD` is the current bootstrap path.
- The harness is intentionally strict about:
  - startup workflow
  - scope control
  - architecture boundaries
  - verification before completion
  - durable repo artifacts over chat summaries

## What Has Not Started Yet

- Android project scaffold
- Gradle setup
- modules and packages
- feature implementation
- tests
- executable `init.sh`

## Current Bootstrap Path

Until the Android scaffold exists, use `docs/SETUP.MD` as the startup and environment guide.

## Recommended Next Step

Recommended next work:

1. keep `docs/PROGRESS.MD` current
2. keep `docs/SESSION_HANDOFF.MD` current
3. keep `docs/SETUP.MD` current when startup assumptions change
4. scaffold the Android project structure aligned to `docs/ARCHITECTURE.MD`
5. create `init.sh` after the Gradle wrapper and baseline tasks exist

Do not create a fake `init.sh` before the Android scaffold exists.

## Required Reading Order For Next Session

1. `AGENTS.MD`
2. `docs/PRODUCT.MD`
3. `docs/ARCHITECTURE.MD`
4. `feature_list.json`
5. `docs/SETUP.MD`
6. `docs/ecommerce-android-plan.md`
7. inspect the current active feature in `feature_list.json` before implementation

## Important Constraints

- Stay inside the current MVP scope.
- Do not introduce cart, checkout, orders, payments, or roles.
- Do not bypass `feature_list.json` dependencies.
- Work one feature at a time.
- Do not mark any feature done without verification evidence.
- Treat `docs/SETUP.MD` as the bootstrap truth until a real `init.sh` replaces it.
