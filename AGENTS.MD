# AGENTS.MD — ZY-Commerce Android

This repository is being prepared for long-running AI-assisted Android development. The goal is not to maximize short-term code output. The goal is to leave the repository in a state where the next session can continue safely, with minimal guessing, and without relying on chat history.

If any rule here conflicts with `docs/PRODUCT.MD`, `docs/ARCHITECTURE.MD`, `requirements/mvp_requirements_ecommerce.md`, or `requirements/ecommerce-android-plan.md`, prefer those files and update this file when the operating rules need to change.

## Startup Workflow

Before writing code, complete these steps in order:

1. Confirm the working directory with `pwd`.
2. Read this file completely.
3. Read `docs/PRODUCT.MD`.
4. Read `docs/ARCHITECTURE.MD`.
5. Read `docs/RELIABILITY.MD`.
6. Read `feature_list.json`.
7. Read `.harness/docs/PROGRESS.MD` for the current active feature or harness task.
8. Read `.harness/docs/SETUP.MD` for the current bootstrap path.
9. Read `.harness/docs/TESTING_STRATEGY.MD` for testing coverage expectations.
10. Read `.harness/docs/VERIFICATION.MD` for the current verification path and evidence expectations.
11. Read `.harness/docs/BACKEND_LOCAL.MD` when backend-dependent behavior or smoke checks are relevant.
12. Read `requirements/ecommerce-android-plan.md` if feature scope, dependencies, or sprint order are unclear.
13. Review recent commits with `git log --oneline -5`.
14. Run the standard project verification path if it exists.

If the baseline is already failing, repair that first before adding new scope.

## Harness Map

Use the harness files by question, not by habit:

- Product intent or feature meaning: `docs/PRODUCT.MD`
- Architecture boundaries or stack rules: `docs/ARCHITECTURE.MD`
- Reliability, clean-state, benchmark, or lifecycle checks: `docs/RELIABILITY.MD`
- Planned or completed feature state and verification evidence: `feature_list.json`
- Bootstrap steps and local tooling: `.harness/docs/SETUP.MD`
- Backend startup and Android connection rules: `.harness/docs/BACKEND_LOCAL.MD`
- Test coverage expectations: `.harness/docs/TESTING_STRATEGY.MD`
- Verification commands and evidence format: `.harness/docs/VERIFICATION.MD`
- Durable project or harness decisions: `.harness/docs/DECISIONS.MD`
- Active feature only: `.harness/docs/PROGRESS.MD`
- Completed feature or harness history: `.harness/docs/HISTORY.MD`
- Next-session restart note only: `.harness/docs/SESSION_HANDOFF.MD`
- Multi-agent operating flow: `.harness/docs/AGENT_WORKFLOW.MD`
- Role prompts: `.harness/agents/`

Keep ownership sharp:

- Update product docs when behavior changes.
- Update architecture docs when structure or technical rules change.
- Update `docs/RELIABILITY.MD`, `.harness/docs/VERIFICATION.MD`, or `.harness/docs/clean-state-checklist.md` when lifecycle or verification rules change.
- Update `feature_list.json` when feature status or evidence changes.
- Keep only the active item in `.harness/docs/PROGRESS.MD`.
- Keep completed summaries in `.harness/docs/HISTORY.MD`, newest first.
- Keep `.harness/docs/SESSION_HANDOFF.MD` short and restart-oriented.

## Product Scope

`docs/PRODUCT.MD` is the source of truth for current MVP scope, out-of-scope boundaries, intended users, and success criteria.

Do not restate or extend product scope in this file. If scope changes, update `docs/PRODUCT.MD` first.

## Working Rules

- Work on exactly one feature at a time.
- Select the feature from `feature_list.json`.
- Do not silently expand scope because a nearby improvement seems convenient.
- Do not mark a feature done because code was written. A feature is done only after verification and evidence.
- Do not refactor unrelated areas before the selected feature is functionally verified.
- Prefer durable repository artifacts over chat-only summaries.
- If a rule in the docs and a file in the codebase disagree, stop and resolve the inconsistency explicitly.

## Architecture Rules

The Android app follows the architecture in `docs/ARCHITECTURE.MD`. Treat that file as the source of truth for layer ownership, dependency rules, stack choices, and module boundaries.

`.harness/docs/DECISIONS.MD` records durable decisions and rationale. Do not introduce major new libraries, replace the stack, or redesign module boundaries without updating both documents.

## Scope Control Rules

- The feature list at repo root is the source of truth for execution scope.
- Dependencies in `feature_list.json` must be respected.
- If a feature depends on unfinished work, do not bypass that dependency unless the user explicitly approves a plan change.
- Keep Sprint 1 and Sprint 2 boundaries visible when planning execution.

If scope is ambiguous:

1. Re-read `feature_list.json`.
2. Re-read the relevant section in `docs/PRODUCT.MD`.
3. Re-read the relevant story in `requirements/ecommerce-android-plan.md`.

## Required Artifacts

`.harness/harness_manifest.json` is the canonical required-file inventory for startup, fast verification, cleanup, benchmark, closure, and runtime role-output checks. Scripts read that manifest directly, so update the manifest when the required harness surface changes.

Use this file for the operating map and ownership rules, not as a second required-file list.

Current bootstrap rule:

- Use `init.sh` as the standard startup and verification path. Use `.harness/docs/SETUP.MD` for local tooling, `.harness/docs/BACKEND_LOCAL.MD` for backend checks, `.harness/docs/TESTING_STRATEGY.MD` for coverage expectations, `.harness/docs/VERIFICATION.MD` for evidence expectations, and `.harness/docs/clean-state-checklist.md` plus `docs/RELIABILITY.MD` for lifecycle discipline.

## Verification Philosophy

Completion judgment must be externalized. Do not trust internal confidence. Testing coverage expectations live in `.harness/docs/TESTING_STRATEGY.MD`; commands, evidence expectations, and failure reporting rules live in `.harness/docs/VERIFICATION.MD`.

## Definition Of Done

A feature is done only when all of the following are true:

- Target behavior is implemented.
- The implementation remains inside the selected feature scope.
- Required verification actually ran.
- Evidence is recorded in `feature_list.json` and the active feature notes in `.harness/docs/PROGRESS.MD`.
- Evaluator-approved completion is closed through `scripts/close-cycle.sh`, which archives history, resets progress, updates handoff, and clears runtime role outputs.
- Product and architecture docs were updated if the feature changed them.
- The repository remains restartable for the next session.

For Android features, use `.harness/docs/TESTING_STRATEGY.MD` for coverage expectations and `.harness/docs/VERIFICATION.MD` for the current verification checklist and evidence format.

## Standard Verification Path

Run `./init.sh` by default. See `.harness/docs/VERIFICATION.MD` for the underlying Gradle checks, optional additions, backend-dependent smoke checks, and evidence format.

For a fast pre-edit startup-surface check, use `bash scripts/verify-fast.sh`.

## Error Feedback Rules

When a check fails, record actionable repair information. See `.harness/docs/VERIFICATION.MD` for failure-reporting examples.

## Multi-Agent Upgrade Path

Use a single implementing agent by default. Upgrade to explicit Planner, Implementer, Reviewer, and Evaluator roles when feature risk or complexity justifies it. The workflow lives in `.harness/docs/AGENT_WORKFLOW.MD`, and role prompts live in `.harness/agents/`. Human-in-the-loop approval is required before each role hands off to the next role. Runtime handoff outputs live under `.harness/agent_outputs/` and are cleared after feature completion.

## Session Continuity

Before ending a session:

1. Update `feature_list.json`.
2. Update `.harness/docs/PROGRESS.MD` for the active feature or task if it exists.
3. Update `.harness/docs/SESSION_HANDOFF.MD` if continuity changed.
4. If the feature or harness task is ready to close, route closure through the Evaluator-approved `scripts/close-cycle.sh` path instead of hand-editing repeated state files.
5. Record unresolved blockers, risks, or follow-ups.
6. Leave the repository clean enough that the next session can start from the standard path immediately.

For Evaluator-approved feature completion, prefer the scripted closure path over hand-editing repeated state files:

```bash
scripts/close-cycle.sh --approved --feature <ID> --summary "..." --verification "..." --note "..."
```

Run `bash scripts/verify-fast.sh` after closure.

If a feature is partially complete:

- mark it `in_progress` or `blocked`
- do not relabel it as done to make the repository look cleaner

## Commit Hygiene

- Prefer small commits aligned to one feature or one safe checkpoint.
- Do not mix multiple unrelated features in the same change set.
- Do not commit generated noise, secrets, or broken intermediate state.

## Escalation Rules

Stop and ask for approval before:

- changing app scope beyond the current MVP
- introducing new major libraries not already aligned with `docs/ARCHITECTURE.MD`
- redesigning module boundaries
- adding out-of-scope commerce features
- changing auth assumptions such as roles, refresh tokens, or protected/public endpoint behavior

## Final Rule

The harness is part of the product delivery system. Preserve it as carefully as the code.
