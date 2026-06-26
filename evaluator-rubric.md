# Evaluator Rubric — ZY-Commerce Android

## Purpose

Use this rubric when evaluating whether a feature or harness task is truly complete. This is an Android-specific scoring guide, not a replacement for story evidence in `feature_list.json`.

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

As of 2026-06-25:

- `ST-01` and `ST-02` are implemented and verified.
- `ST-03` Sign in is the next planned feature.
- Catalog and product-admin modules still contain placeholders.
- Session persistence, automated Android UI regression, and backend contract replay are not yet complete.

## Current Repository Assessment

| Criterion | Score | Notes |
|-----------|-------|-------|
| Scope Control | 4 | Strong feature list and harness discipline; product scope still partially repeated in `AGENTS.MD` |
| Build And Static Checks | 4 | Standard Gradle verification path exists through `./init.sh` |
| Architecture Boundaries | 4 | Clean Architecture rules are explicit; some modules are still placeholders |
| Backend Contract Confidence | 3 | Auth contract is partially exercised; broader catalog/session confidence is still future work |
| Error Mapping Quality | 4 | Registration and duplicate-email error mapping are intentionally handled |
| Test Depth | 4 | Domain, repository, and ViewModel coverage exist for the current auth slice |
| User-Facing Behavior | 3 | Registration path is present; sign-in and broader catalog UX are still incomplete |
| Session Persistence Readiness | 2 | Architecture expectations exist, but session implementation is not complete yet |
| Documentation Alignment | 4 | Product, architecture, reliability, and harness docs are present and better scoped |
| Evidence Quality | 4 | `feature_list.json` includes real verification evidence for completed stories |
| Restartability | 4 | Startup path, cleanup scanner, and continuity artifacts are in place |
| Harness Completeness | 4 | Capstone-style evaluation artifacts now exist; later phases should deepen runtime-aware checks |

## Overall Baseline

Current baseline: **3.7 / 5**

Interpretation:

- The harness is solid and clearly above a minimal setup.
- The biggest remaining quality gaps are session persistence readiness, broader backend confidence, and runtime-aware verification depth.

## Phase 1 Exit Criteria

Phase 1 should be considered complete when:

- `evaluator-rubric.md` and `quality-document.md` exist
- `AGENTS.MD` and `init.sh` recognize the new artifacts if they are required
- focused structural verification passes
- the new artifacts stay Android-specific and point to existing source-of-truth docs without requiring a separate quick-reference file
