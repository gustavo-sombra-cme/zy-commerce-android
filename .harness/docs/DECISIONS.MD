# DECISIONS.MD — ZY-Commerce Android

This file records durable project and harness decisions. Keep it short: it should explain what was decided, why it matters, and where to look for operational details.

## D-001 — Keep A Restartable Harness Surface

Decision: The repository should preserve enough written context that future sessions can continue without relying on chat history.

Reason: This project is intended for long-running AI-assisted development. Durable repo artifacts reduce guessing and make feature work auditable.

Operational files:

- `AGENTS.MD` for agent workflow rules
- `.harness/docs/SETUP.MD` for local bootstrap and tooling
- `.harness/docs/BACKEND_LOCAL.MD` for local backend startup and Android connection details
- `.harness/docs/backend-installation-instructions.md` for backend macOS installation and run guidance
- `.harness/docs/TESTING_STRATEGY.MD` for test coverage expectations and test-level ownership
- `.harness/docs/VERIFICATION.MD` for verification commands, evidence, and failure reporting
- `.harness/docs/PROGRESS.MD` for the active feature or harness task
- `.harness/docs/HISTORY.MD` for completed feature and harness history, newest first
- `.harness/docs/SESSION_HANDOFF.MD` for a minimal next-session restart note
- `requirements/` for source requirements, backlog, source design, and backend baseline
- `feature_list.json` for feature status and evidence

## D-002 — Use The Existing Documentation Hierarchy

Decision: Keep product, architecture, setup, active progress, archived history, handoff, and feature status in separate documents instead of one large file.

Reason: Each document answers a different question and can stay focused.

Document responsibilities:

- `docs/PRODUCT.MD`: product intent, users, scope, non-goals
- `docs/ARCHITECTURE.MD`: technical design, layers, modules
- `requirements/mvp_requirements_ecommerce.md`: business requirements baseline
- `requirements/ecommerce-android-plan.md`: stories, backlog, dependencies, sprint plan
- `requirements/ecommerce-android-tech-design.md`: original Android technical design source
- `requirements/zy-commerce-backend-business-baseline.md`: backend business baseline
- `.harness/docs/SETUP.MD`: bootstrap and local tooling
- `.harness/docs/BACKEND_LOCAL.MD`: local backend startup, URLs, Android connection path
- `.harness/docs/backend-installation-instructions.md`: backend macOS installation and run guide
- `.harness/docs/TESTING_STRATEGY.MD`: test coverage model and test-level ownership
- `.harness/docs/VERIFICATION.MD`: verification commands, evidence, smoke checks, failure reporting
- `.harness/docs/DECISIONS.MD`: durable decisions and rationale
- `.harness/docs/PROGRESS.MD`: active feature or active harness task only
- `.harness/docs/HISTORY.MD`: completed feature and harness archive, newest first
- `feature_list.json`: planned, active, blocked, or done feature state

## D-003 — Work One Feature At A Time

Decision: Select exactly one feature from `feature_list.json` for implementation work.

Reason: The MVP has dependencies between auth, catalog, and product-management stories. Single-feature execution keeps scope and verification clear.

Implications:

- Do not silently implement dependent stories.
- Do not mark a story done without verification evidence.
- Record active feature progress in `feature_list.json` and `.harness/docs/PROGRESS.MD`.
- When the feature is done, archive the completed progress summary at the top of `.harness/docs/HISTORY.MD` and reset `.harness/docs/PROGRESS.MD` to its template.

## D-004 — Use Clean Architecture Boundaries

Decision: Keep Presentation, Domain, and Data responsibilities separate.

Reason: The app will grow from registration into auth/session, catalog, and product-management flows. Clear boundaries reduce coupling and make tests easier to target.

Boundary rules:

- Presentation owns Compose UI, navigation, screen state, effects, and ViewModels.
- Domain owns use cases, domain models, and repository interfaces.
- Data owns repository implementations, Retrofit APIs, Room entities/DAOs, DataStore, DTOs, and mappers.
- Domain must not depend on Android framework, Retrofit, Room, Hilt, or Compose.
- UI must not call Retrofit, Room, or repositories directly from composables.
- DTOs and Room entities must not leak into UI.

## D-005 — Use The MVP Android Stack

Decision: Use Kotlin, Jetpack Compose, Material 3, MVVM, Hilt, Coroutines/Flow, Retrofit/OkHttp, Kotlinx Serialization, Room, DataStore, and Paging 3.

Reason: This is the stack already defined by `docs/ARCHITECTURE.MD` and fits the MVP backlog.

Change rule: Do not introduce major new libraries or replace this stack without updating architecture docs and recording the decision here.

## D-006 — Keep The Scaffold Modular

Decision: Keep the committed Android scaffold aligned to the planned module layout.

Current modules:

```text
app/
core/common/
core/designsystem/
core/network/
core/database/
core/storage/
domain/auth/
domain/catalog/
data/auth/
data/catalog/
feature/auth/
feature/catalog/
feature/productadmin/
```

Reason: This matches the architecture document while keeping future catalog and product-admin work from being forced into auth modules.

## D-007 — Use `init.sh` As The Local Bootstrap Path

Decision: `./init.sh` is the standard local startup and verification command.

Reason: The repo now has an Android scaffold and Gradle wrapper, so startup should be executable instead of documentation-only.

Operational details live in `.harness/docs/SETUP.MD`. Testing strategy lives in `.harness/docs/TESTING_STRATEGY.MD`; verification details live in `.harness/docs/VERIFICATION.MD`.

## D-008 — Target Local Backend Through Emulator-Friendly Debug Config

Decision: Debug Android builds target the local backend through the emulator-friendly local URL and allow debug-only cleartext for local hosts.

Reason: Emulator and physical-device networking differ, and release builds must not assume local HTTP.

Backend startup, URLs, device rules, and smoke prerequisites live in `.harness/docs/BACKEND_LOCAL.MD`.

## D-009 — Externalize Verification Evidence

Decision: Completion requires evidence, not internal confidence.

Reason: Features can appear done while compile, lint, unit behavior, integration behavior, or manual smoke paths are still failing.

Coverage expectations live in `.harness/docs/TESTING_STRATEGY.MD`; evidence format and verification commands live in `.harness/docs/VERIFICATION.MD`.

## D-010 — Use Multi-Agent Roles For Risky Work

Decision: Use a single agent by default. Introduce explicit Planner, Implementer, Reviewer, and Evaluator roles when feature risk, complexity, or code-quality risk justifies it.

Reason: Role separation is useful for auth/session, catalog pagination/search/filter, product write flows, backend contract uncertainty, broad refactors, code-quality review, and failing verification. It keeps planning, implementation, and completion judgment independent enough to reduce drift.

Operational workflow lives in `.harness/docs/AGENT_WORKFLOW.MD`. Role prompts live in `.harness/agents/`. Human approval is required before each handoff between roles. Runtime handoff outputs live under `.harness/agent_outputs/` and are cleared after a feature is complete.

## D-011 — ST-01 Completion Scope

Decision: `ST-01` implements registration and navigates to a sign-in placeholder after success.

Reason: The story requires account creation and next-step navigation, but actual sign-in belongs to `ST-03` and duplicate-email-specific UX belongs to `ST-02`.

Implemented behavior:

- `RegisterScreen` accepts email and password.
- `RegisterUserUseCase` validates required inputs and trims email.
- `AuthRepositoryImpl` posts to `POST /api/auth/users/register`.
- DTO mapping expects `userId` and `email`.
- Success shows confirmation and routes to the sign-in placeholder.

## D-012 — Separate Testing Strategy From Verification Commands

Decision: Keep test coverage expectations in `.harness/docs/TESTING_STRATEGY.MD` and command/evidence rules in `.harness/docs/VERIFICATION.MD`.

Reason: Testing strategy changes at a different pace than local verification commands. Separating them keeps coverage decisions easy to find without duplicating command details.
