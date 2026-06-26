# DECISIONS.MD — ZY-Commerce Android

This file records durable project and harness decisions only. Do not use it for feature history, session notes, or story-specific implementation summaries.

## D-001 — Keep A Restartable Harness Surface

Decision: The repository must preserve enough written context that future sessions can continue without relying on chat history.

Reason: This is a long-running AI-assisted project. Durable repo artifacts reduce guessing and make feature work auditable.

Operational details live in:

- `AGENTS.MD`
- `.harness/docs/SETUP.MD`
- `.harness/docs/BACKEND_LOCAL.MD`
- `.harness/docs/TESTING_STRATEGY.MD`
- `.harness/docs/VERIFICATION.MD`
- `.harness/docs/clean-state-checklist.md`
- `.harness/docs/PROGRESS.MD`
- `.harness/docs/HISTORY.MD`
- `.harness/docs/SESSION_HANDOFF.MD`
- `.harness/harness_manifest.json`
- `feature_list.json`

## D-002 — Keep Documentation Ownership Split By Question

Decision: Keep product, architecture, reliability, setup, verification, decisions, active progress, archive history, and handoff notes in separate documents.

Reason: Each document changes at a different pace and answers a different operational question.

Ownership summary:

- `docs/PRODUCT.MD`: product intent, scope, and user-facing behavior
- `docs/ARCHITECTURE.MD`: technical design, layers, and module boundaries
- `docs/RELIABILITY.MD`: lifecycle, clean-state, benchmark, and cleanup expectations
- `.harness/docs/SETUP.MD`: bootstrap and local tooling
- `.harness/docs/BACKEND_LOCAL.MD`: backend startup, URLs, and Android connection path
- `.harness/docs/TESTING_STRATEGY.MD`: coverage model and test-level ownership
- `.harness/docs/VERIFICATION.MD`: commands, evidence format, and failure reporting
- `.harness/docs/PROGRESS.MD`: active feature or harness task only
- `.harness/docs/HISTORY.MD`: completed feature and harness archive
- `.harness/docs/SESSION_HANDOFF.MD`: short next-session restart note
- `.harness/harness_manifest.json`: required-file sets consumed by harness scripts
- `feature_list.json`: feature state and durable verification evidence

## D-003 — Work One Feature At A Time

Decision: Select exactly one feature from `feature_list.json` for implementation work.

Reason: The MVP backlog has dependencies across auth, catalog, and product-management stories. Single-feature execution keeps scope and verification clear.

Implications:

- do not silently implement dependent stories
- do not mark a story done without verification evidence
- keep active execution detail in `.harness/docs/PROGRESS.MD`
- keep completed summaries in `.harness/docs/HISTORY.MD`

## D-004 — Use Clean Architecture Boundaries

Decision: Keep Presentation, Domain, and Data responsibilities separate.

Reason: The app will grow from registration into auth/session, catalog, and product-management flows. Clear boundaries reduce coupling and make tests easier to target.

Boundary rules:

- Presentation owns Compose UI, navigation, screen state, effects, and ViewModels.
- Domain owns use cases, domain models, and repository interfaces.
- Data owns repository implementations, Retrofit APIs, Room entities and DAOs, DataStore, DTOs, and mappers.
- Domain must not depend on Android framework, Retrofit, Room, Hilt, or Compose.
- UI must not call Retrofit, Room, or repositories directly from composables.
- DTOs and Room entities must not leak into UI.

## D-005 — Use The MVP Android Stack

Decision: Use Kotlin, Jetpack Compose, Material 3, MVVM, Hilt, Coroutines and Flow, Retrofit and OkHttp, Kotlinx Serialization, Room, DataStore, and Paging 3.

Reason: This stack is already defined by `docs/ARCHITECTURE.MD` and fits the MVP backlog.

Change rule: Do not introduce major new libraries or replace this stack without updating `docs/ARCHITECTURE.MD` and recording the new decision here.

## D-006 — Keep The Scaffold Modular

Decision: Keep the committed Android scaffold aligned to the planned module layout.

Reason: This matches the architecture document while keeping future catalog and product-admin work from being forced into auth modules.

## D-007 — Use `init.sh` As The Local Bootstrap Path

Decision: `./init.sh` is the standard local startup and verification command.

Reason: The repo now has an Android scaffold and Gradle wrapper, so startup should be executable and repeatable.

Operational details live in `.harness/docs/SETUP.MD`, `.harness/docs/VERIFICATION.MD`, `.harness/docs/clean-state-checklist.md`, and `docs/RELIABILITY.MD`.

## D-008 — Target Local Backend Through Debug-Only Local Configuration

Decision: Debug Android builds target the local backend through emulator-friendly local URLs and allow cleartext only for approved local hosts.

Reason: Emulator and physical-device networking differ, and release builds must not assume local HTTP.

Operational details live in `.harness/docs/BACKEND_LOCAL.MD`.

## D-009 — Externalize Verification Evidence

Decision: Completion requires evidence, not internal confidence.

Reason: Features can appear done while compile, lint, unit behavior, integration behavior, or smoke paths are still failing.

Coverage expectations live in `.harness/docs/TESTING_STRATEGY.MD`. Evidence format and verification commands live in `.harness/docs/VERIFICATION.MD`.

## D-010 — Use Multi-Agent Roles For Risky Work

Decision: Use a single agent by default. Introduce explicit Planner, Implementer, Reviewer, and Evaluator roles when feature risk, complexity, or code-quality risk justifies it.

Reason: Role separation is useful for auth and session work, catalog pagination or filtering, product write flows, backend contract uncertainty, broad refactors, and failing verification.

Operational workflow lives in `.harness/docs/AGENT_WORKFLOW.MD`. Role prompts live in `.harness/agents/`. Runtime handoff outputs live under `.harness/agent_outputs/`.

## D-011 — Keep Testing Strategy Separate From Verification Commands

Decision: Keep test coverage expectations in `.harness/docs/TESTING_STRATEGY.MD` and command and evidence rules in `.harness/docs/VERIFICATION.MD`.

Reason: Coverage strategy changes at a different pace than local command details, benchmark expectations, or cleanup checks.
