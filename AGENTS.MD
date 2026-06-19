# AGENTS.MD — ZY-Commerce Android

This repository is being prepared for long-running AI-assisted Android development. The goal is not to maximize short-term code output. The goal is to leave the repository in a state where the next session can continue safely, with minimal guessing, and without relying on chat history.

If any rule here conflicts with `docs/PRODUCT.MD`, `docs/ARCHITECTURE.MD`, `docs/mvp_requirements_ecommerce.md`, or `docs/ecommerce-android-plan.md`, prefer those files and update this file when the operating rules need to change.

## Startup Workflow

Before writing code, complete these steps in order:

1. Confirm the working directory with `pwd`.
2. Read this file completely.
3. Read `docs/PRODUCT.MD`.
4. Read `docs/ARCHITECTURE.MD`.
5. Read `feature_list.json`.
6. Read `docs/SETUP.MD` for the current bootstrap and backend-connection path.
7. Read `docs/ecommerce-android-plan.md` if feature scope, dependencies, or sprint order are unclear.
8. Review recent commits with `git log --oneline -5`.
9. Run the standard project verification path if it exists.

If the baseline is already failing, repair that first before adding new scope.

## Docs Hierarchy

The repository documentation is organized for agent readability:

```text
docs/
  PRODUCT.MD                   -- Product intent, users, scope, non-goals
  ARCHITECTURE.MD              -- Technical design, layers, modules, testing strategy
  mvp_requirements_ecommerce.md -- Business requirements baseline
  ecommerce-android-plan.md    -- Stories, backlog, dependencies, sprint plan
  SETUP.MD                     -- Current bootstrap, backend startup, and environment path

repo root/
  AGENTS.MD                    -- Startup rules and execution constraints
  feature_list.json            -- Feature state source of truth
```

Use this hierarchy consistently:

- `PRODUCT.MD` answers what the app is for and what features mean.
- `ARCHITECTURE.MD` answers how the app must be built.
- `feature_list.json` at repo root answers what is planned, active, blocked, or done.
- `SETUP.MD` answers how to bootstrap the repository in its current state.

When behavior changes, update product docs.
When structure or technical rules change, update architecture docs.
When progress changes, update `feature_list.json` and session continuity artifacts.

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

Required stack unless explicitly changed in docs first:

- Kotlin
- Jetpack Compose + Material 3
- Clean Architecture
- MVVM with unidirectional state flow
- Hilt
- Coroutines + Flow
- Retrofit + OkHttp + Kotlinx Serialization
- Room
- DataStore
- Paging 3 for product list scalability

## Scope Control Rules

- The feature list at repo root is the source of truth for execution scope.
- Dependencies in `feature_list.json` must be respected.
- If a feature depends on unfinished work, do not bypass that dependency unless the user explicitly approves a plan change.
- Keep Sprint 1 and Sprint 2 boundaries visible when planning execution.

If scope is ambiguous:

1. Re-read `feature_list.json`.
2. Re-read the relevant section in `docs/PRODUCT.MD`.
3. Re-read the relevant story in `docs/ecommerce-android-plan.md`.

## Required Artifacts

The harness currently requires:

- `docs/PRODUCT.MD`
- `docs/ARCHITECTURE.MD`
- `feature_list.json`
- `docs/SETUP.MD`

The following should exist before or during active implementation:

- `docs/PROGRESS.MD` — session continuity log
- `docs/SESSION_HANDOFF.MD` — short resume notes for next session
- `init.sh` or an equivalent setup/verification script once the Android project scaffold exists

If these artifacts do not exist yet, create them when implementation begins or when they are needed to preserve continuity.

Current bootstrap rule:

- Until a real `init.sh` exists, use `docs/SETUP.MD` as the source of truth for startup, backend checks, and environment preparation.

## Verification Philosophy

Completion judgment must be externalized. Do not trust the agent's internal confidence.

Every feature should be verified across three layers where applicable:

1. **Syntax / static layer**
   - code compiles
   - lint/type/static checks pass
2. **Behavior layer**
   - unit and integration expectations pass
   - state transitions and error handling are correct
3. **System layer**
   - user-facing flow actually works in the app
   - protected/public behavior matches requirements

Do not skip directly from "code written" to "done."

## Definition Of Done

A feature is done only when all of the following are true:

- Target behavior is implemented.
- The implementation remains inside the selected feature scope.
- Required verification actually ran.
- Evidence is recorded in `feature_list.json` and/or `docs/PROGRESS.MD`.
- Product and architecture docs were updated if the feature changed them.
- The repository remains restartable for the next session.

For Android features, the minimum verification target should usually include:

- successful build path
- relevant unit tests
- relevant integration tests where infrastructure is involved
- manual or automated UI verification for user-visible behavior

## Standard Verification Path

When the Android Gradle project exists, the default verification path should be:

```bash
./gradlew test
./gradlew lintDebug
./gradlew assembleDebug
```

Add these when available and relevant:

- integration-test tasks
- screenshot regression tasks
- Compose UI test tasks
- emulator/manual smoke validation notes

Until the Android project scaffold exists, treat documentation consistency and feature-scope correctness as the current baseline checks.

## Error Feedback Rules

When a check fails, record actionable repair information.

Bad:

- "Test failed."

Good:

- "Login screen UI test failed: sign-in success did not navigate to catalog. Check session persistence and navigation effect handling in `LoginViewModel` and sign-in route."

Verification feedback should help the next agent self-correct without guessing.

## Multi-Agent Upgrade Path

Default mode for now:

- use a single implementing agent guided by this harness

Upgrade to explicit role separation when tasks become risky, multi-step, or prone to premature completion.

Recommended future roles:

- **Planner**
  - expands short requests into scoped execution contracts
  - defines acceptance criteria, exclusions, and verification expectations
- **Generator**
  - implements exactly one scoped feature block
- **Evaluator**
  - verifies the output against acceptance criteria and evidence
  - should not be the same reasoning pass that wrote the feature

Use planner + generator + evaluator especially for:

- auth/session work
- catalog pagination/search/filter correctness
- product create/update/deactivate flows
- refactors affecting multiple modules

Do not introduce multi-agent orchestration by default before the repo has enough harness surface to support it cleanly.

## Session Continuity

Before ending a session:

1. Update `feature_list.json`.
2. Update `docs/PROGRESS.MD` if it exists.
3. Update `docs/SESSION_HANDOFF.MD` if it exists.
4. Record unresolved blockers, risks, or follow-ups.
5. Leave the repository clean enough that the next session can start from the standard path immediately.

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
