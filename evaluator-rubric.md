# Evaluator Rubric — ZY-Commerce Android

## Purpose

Use this rubric when evaluating whether a feature or harness task is truly complete. This is an Android-specific scoring guide, not a replacement for story evidence in `feature_list.json`.

Last updated: 2026-06-30

## Scoring Model

Use a 1-5 scale:

- 1 = failing or missing
- 2 = partial and risky
- 3 = acceptable with visible gaps
- 4 = strong with minor residual risk
- 5 = complete, verified, and restartable

## Evaluation Criteria

| Criterion | What To Check |
|-----------|---------------|
| Scope Control | Only the selected story or harness task changed; dependencies respected |
| Build And Static Checks | Relevant Gradle checks pass; lint and compile state are clean enough for the selected work |
| Architecture Boundaries | Presentation, Domain, Data, and Core responsibilities remain intact |
| Backend Contract Confidence | Retrofit/API assumptions match the known backend contract or are explicitly recorded as uncertain |
| Error Mapping Quality | Validation, conflict, auth, and unknown failures are handled intentionally |
| Test Depth | Tests were added or updated at the closest useful level |
| User-Facing Behavior | Screens, state, effects, and protected-action gating behave as expected |
| Session Persistence Readiness | Session and token handling remain aligned with current architecture expectations |
| Documentation Alignment | Product, architecture, reliability, and harness docs still match the implementation |
| Evidence Quality | `feature_list.json` and continuity docs record what actually ran and what was skipped |
| Restartability | The next session can restart from `AGENTS.MD` and `./init.sh` without guessing |
| Harness Completeness | Required harness artifacts exist and remain coherent |

## Current Baseline Snapshot

As of 2026-06-30:

- `ST-01`, `ST-02`, `ST-03`, and `ST-04` are implemented and verified through targeted auth checks.
- `ST-05` View current profile is the next planned feature.
- Catalog and product-admin modules still contain placeholders.
- Manual backend/emulator smoke for sign-in, sign-in failure states, session restoration, automated Android UI regression, and backend contract replay are not yet complete.

## Current Repository Assessment

| Criterion | Score | Notes |
|-----------|-------|-------|
| Scope Control | 4 | Strong feature list and harness discipline; required-file ownership now routes through `.harness/harness_manifest.json` |
| Build And Static Checks | 4 | Standard Gradle verification path exists through `./init.sh` |
| Architecture Boundaries | 4 | Clean Architecture rules are explicit; some modules are still placeholders |
| Backend Contract Confidence | 3 | Auth contract is partially exercised; broader catalog confidence and backend/device smoke are still future work |
| Error Mapping Quality | 4 | Registration, duplicate-email, sign-in failure, and session-restore error handling are intentionally handled |
| Test Depth | 4 | Domain, repository, ViewModel, login error, and session-restore coverage exist for the current auth slice |
| User-Facing Behavior | 3 | Registration, sign-in, and sign-in failure paths are present; profile and broader catalog UX are still incomplete |
| Session Persistence Readiness | 4 | Secure persisted session restore exists; backend/device smoke and invalid-token recovery paths still need deeper coverage |
| Documentation Alignment | 4 | Product, architecture, reliability, and harness docs are present and better scoped |
| Evidence Quality | 4 | `feature_list.json` includes real verification evidence for completed stories |
| Restartability | 4 | Startup path, cleanup scanner, and continuity artifacts are in place |
| Harness Completeness | 4 | Capstone-style evaluation artifacts, manifest-driven checks, and scripted cycle closure exist; later phases should deepen runtime-aware checks |

## Overall Baseline

Current baseline: **4.0 / 5**

Interpretation:

- The harness is solid and clearly above a minimal setup.
- The biggest remaining quality gaps are backend/device smoke coverage, broader catalog confidence, and runtime-aware verification depth.

## Snapshot Rule

This rubric may include a dated repository assessment, but `feature_list.json` remains the canonical source for story status and verification evidence. Update this snapshot after significant feature, verification, or harness changes.
