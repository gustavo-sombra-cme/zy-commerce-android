# AGENTS.MD — ZY-Commerce Android

This repository is being prepared for long-running AI-assisted Android development. The goal is not to maximize short-term code output. The goal is to leave the repository in a state where the next session can continue safely, with minimal guessing, and without relying on chat history.

If any rule here conflicts with `docs/PRODUCT.MD`, `docs/ARCHITECTURE.MD`, `requirements/mvp_requirements_ecommerce.md`, or `requirements/ecommerce-android-plan.md`, prefer those files and update this file when the operating rules need to change.

## Startup Workflow

Before writing code, complete these steps in order:

1. Confirm the working directory with `pwd`.
2. Read this file completely.
3. Read `docs/PRODUCT.MD`.
4. Read `docs/ARCHITECTURE.MD`.
5. Read `feature_list.json`.
6. Read `.harness/docs/PROGRESS.MD` for the current active feature or harness task.
7. Read `.harness/docs/SETUP.MD` for the current bootstrap path.
8. Read `.harness/docs/BACKEND_LOCAL.MD` for local backend connection and smoke-test prerequisites.
9. Read `.harness/docs/TESTING_STRATEGY.MD` for testing coverage expectations.
10. Read `.harness/docs/VERIFICATION.MD` for the current verification path and evidence expectations.
11. Read `requirements/ecommerce-android-plan.md` if feature scope, dependencies, or sprint order are unclear.
12. Review recent commits with `git log --oneline -5`.
13. Run the standard project verification path if it exists.

If the baseline is already failing, repair that first before adding new scope.

## Docs Hierarchy

The repository documentation is organized for agent readability:

```text
docs/
  PRODUCT.MD                   -- Product intent, users, scope, non-goals
  ARCHITECTURE.MD              -- Technical design, layers, and modules

.harness/agents/
  PLANNER.MD                   -- Planning role prompt
  IMPLEMENTER.MD               -- Implementation role prompt
  REVIEWER.MD                  -- Code review role prompt
  EVALUATOR.MD                 -- Evaluation role prompt

.harness/docs/
  SETUP.MD                     -- Current bootstrap and local tooling
  BACKEND_LOCAL.MD             -- Local backend startup and Android connection path
  backend-installation-instructions.md -- Backend macOS installation and run guide
  TESTING_STRATEGY.MD          -- Test coverage model and test-level ownership
  VERIFICATION.MD              -- Verification commands, evidence, and failure reporting
  DECISIONS.MD                 -- Durable project and harness decisions
  AGENT_WORKFLOW.MD            -- Planner, Implementer, Reviewer, Evaluator workflow
  PROGRESS.MD                  -- Active feature or harness task tracker
  HISTORY.MD                   -- Completed feature and harness history, newest first
  SESSION_HANDOFF.MD           -- Minimal restart note for the next session

requirements/
  mvp_requirements_ecommerce.md -- Business requirements baseline
  ecommerce-android-plan.md    -- Stories, backlog, dependencies, sprint plan
  ecommerce-android-tech-design.md -- Original Android technical design source
  zy-commerce-backend-business-baseline.md -- Backend business baseline

repo root/
  AGENTS.MD                    -- Startup rules and execution constraints
  feature_list.json            -- Feature state source of truth
```

Use this hierarchy consistently:

- `PRODUCT.MD` answers what the app is for and what features mean.
- `ARCHITECTURE.MD` answers how the app must be built.
- `feature_list.json` at repo root answers what is planned, active, blocked, or done.
- `SETUP.MD` answers how to bootstrap the repository in its current state.
- `BACKEND_LOCAL.MD` answers how the Android app connects to the local backend.
- `backend-installation-instructions.md` answers how to install and run the backend on macOS.
- `TESTING_STRATEGY.MD` answers what kinds of tests should exist and where they belong.
- `VERIFICATION.MD` answers how to verify changes and record evidence.
- `DECISIONS.MD` records durable decisions and rationale.
- `PROGRESS.MD` tracks the active feature or active harness task only.
- `HISTORY.MD` preserves completed feature and harness history in newest-first order.
- `SESSION_HANDOFF.MD` keeps a minimal next-session restart note only.

When behavior changes, update product docs.
When structure or technical rules change, update architecture docs.
When progress changes, update `feature_list.json` and session continuity artifacts.
Keep active feature notes in `.harness/docs/PROGRESS.MD`.
Keep completed feature and harness history in `.harness/docs/HISTORY.MD`, with the newest record first.

## Current Project Context

This Android app currently targets a narrow MVP backed by the existing ZY-Commerce backend.

In scope now:

- account registration
- sign in
- current signed-in user profile
- public product catalog list
- product search
- product active/inactive filter
- product pagination
- product detail
- authenticated product create
- authenticated product update
- authenticated product deactivation

Out of scope unless explicitly approved:

- shopping cart
- checkout
- orders
- payments
- pricing engine
- inventory management
- product images
- categories
- reviews
- shipping flows
- roles and permissions
- refresh-token architecture
- product reactivation

## Working Rules

- Work on exactly one feature at a time.
- Select the feature from `feature_list.json`.
- Do not silently expand scope because a nearby improvement seems convenient.
- Do not mark a feature done because code was written. A feature is done only after verification and evidence.
- Do not refactor unrelated areas before the selected feature is functionally verified.
- Prefer durable repository artifacts over chat-only summaries.
- If a rule in the docs and a file in the codebase disagree, stop and resolve the inconsistency explicitly.

## Architecture Rules

The Android app follows the architecture in `docs/ARCHITECTURE.MD`.

Required boundaries:

- Presentation owns Compose UI, screen state, navigation, and ViewModels.
- Domain owns use cases, domain models, and repository interfaces.
- Data owns repository implementations, Retrofit APIs, Room entities/DAOs, DataStore, and mappers.
- Domain must not depend on Android framework, Retrofit, Room, Hilt, or Compose.
- UI must not call Retrofit, Room, or repositories directly from composables.
- DTOs and Room entities must not leak into UI.

Required stack and rationale are recorded in `docs/ARCHITECTURE.MD` and summarized in `.harness/docs/DECISIONS.MD`. Do not introduce major new libraries or replace the stack without updating both documents.

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

The harness currently requires:

- `docs/PRODUCT.MD`
- `docs/ARCHITECTURE.MD`
- `requirements/mvp_requirements_ecommerce.md`
- `requirements/ecommerce-android-plan.md`
- `requirements/ecommerce-android-tech-design.md`
- `requirements/zy-commerce-backend-business-baseline.md`
- `feature_list.json`
- `.harness/docs/SETUP.MD`
- `.harness/docs/BACKEND_LOCAL.MD`
- `.harness/docs/backend-installation-instructions.md`
- `.harness/docs/TESTING_STRATEGY.MD`
- `.harness/docs/VERIFICATION.MD`
- `.harness/docs/DECISIONS.MD`
- `.harness/docs/AGENT_WORKFLOW.MD`
- `.harness/agents/README.MD`
- `.harness/agents/PLANNER.MD`
- `.harness/agents/IMPLEMENTER.MD`
- `.harness/agents/REVIEWER.MD`
- `.harness/agents/EVALUATOR.MD`

The following should exist before or during active implementation:

- `.harness/docs/PROGRESS.MD` — active feature or active harness task tracker
- `.harness/docs/HISTORY.MD` — completed feature and harness history, newest first
- `.harness/docs/SESSION_HANDOFF.MD` — minimal restart note for the next session
- `init.sh` or an equivalent setup/verification script once the Android project scaffold exists

If these artifacts do not exist yet, create them when implementation begins or when they are needed to preserve continuity.

Current bootstrap rule:

- Use `init.sh` as the standard startup and verification path. Use `.harness/docs/SETUP.MD` for local tooling, `.harness/docs/BACKEND_LOCAL.MD` for backend checks, `.harness/docs/TESTING_STRATEGY.MD` for coverage expectations, and `.harness/docs/VERIFICATION.MD` for evidence expectations.

## Verification Philosophy

Completion judgment must be externalized. Do not trust internal confidence. Testing coverage expectations live in `.harness/docs/TESTING_STRATEGY.MD`; commands, evidence expectations, and failure reporting rules live in `.harness/docs/VERIFICATION.MD`.

## Definition Of Done

A feature is done only when all of the following are true:

- Target behavior is implemented.
- The implementation remains inside the selected feature scope.
- Required verification actually ran.
- Evidence is recorded in `feature_list.json` and the active feature notes in `.harness/docs/PROGRESS.MD`.
- When a feature or harness task is done, its completed progress summary is archived at the top of `.harness/docs/HISTORY.MD` and `.harness/docs/PROGRESS.MD` is reset to the template for the next active item.
- Product and architecture docs were updated if the feature changed them.
- The repository remains restartable for the next session.

For Android features, use `.harness/docs/TESTING_STRATEGY.MD` for coverage expectations and `.harness/docs/VERIFICATION.MD` for the current verification checklist and evidence format.

## Standard Verification Path

Run `./init.sh` by default. See `.harness/docs/VERIFICATION.MD` for the underlying Gradle checks, optional additions, backend-dependent smoke checks, and evidence format.

## Error Feedback Rules

When a check fails, record actionable repair information. See `.harness/docs/VERIFICATION.MD` for failure-reporting examples.

## Multi-Agent Upgrade Path

Use a single implementing agent by default. Upgrade to explicit Planner, Implementer, Reviewer, and Evaluator roles when feature risk or complexity justifies it. The workflow lives in `.harness/docs/AGENT_WORKFLOW.MD`, and role prompts live in `.harness/agents/`. Human-in-the-loop approval is required before each role hands off to the next role. Runtime handoff outputs live under `.harness/agent_outputs/` and are cleared after feature completion.

## Session Continuity

Before ending a session:

1. Update `feature_list.json`.
2. Update `.harness/docs/PROGRESS.MD` for the active feature or task if it exists.
3. If the feature or harness task is done, move its completed progress summary to the top of `.harness/docs/HISTORY.MD` and reset `.harness/docs/PROGRESS.MD` to the template.
4. Update `.harness/docs/SESSION_HANDOFF.MD` if it exists.
5. Record unresolved blockers, risks, or follow-ups.
6. Leave the repository clean enough that the next session can start from the standard path immediately.

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
