# HISTORY.MD

This file keeps the completed progress history for finished features and finished harness tasks.

Archive records in reverse chronological order: newest first, oldest last.

Use `.harness/docs/PROGRESS.MD` for the active feature only.

## Archived History

### 2026-06-29 — ST-04 Show sign-in failure clearly

Completed:

- Delivered clear sign-in failure messaging for invalid credentials and inactive accounts, with repository and ViewModel coverage for the login error paths.

Validation:

- `JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew :domain:auth:test :data:auth:test :feature:auth:test; JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./init.sh; bash scripts/verify-fast.sh`

Notes:

- Manual backend or emulator smoke testing for invalid-credentials and inactive-account flows was not run.

Next feature:

- ST-05 View current profile

### 2026-06-26 — Evaluator Cycle Closure Workflow

Completed:

- added `scripts/close-cycle.sh` as the single Evaluator-approved closure command for marking a feature done, writing completion evidence, archiving history, resetting progress, rewriting handoff, and emptying runtime agent outputs
- updated `.harness/agents/EVALUATOR.MD` so a `pass` decision asks for human approval before running cycle closure
- updated `.harness/docs/AGENT_WORKFLOW.MD` and `AGENTS.MD` to route completion cleanup through the closure script instead of repeated hand edits
- added closure-related required files to `.harness/harness_manifest.json`

Validation:

- `bash -n scripts/close-cycle.sh && bash -n init.sh && bash -n scripts/verify-fast.sh && bash -n scripts/cleanup-scanner.sh && bash -n scripts/harness-benchmark.sh`
- `scripts/close-cycle.sh` without required arguments fails safely and prints usage
- `git diff --check`
- `bash scripts/verify-fast.sh`

Next feature:

- `ST-04` Show sign-in failure clearly

### 2026-06-26 — Harness Drift Cleanup

Completed:

- refreshed `quality-document.md` and `evaluator-rubric.md` so dated snapshots match `ST-03` being complete and `ST-04` being next
- added `.harness/harness_manifest.json` as the central required-file manifest for startup, fast verification, cleanup, benchmark, and runtime role-output checks
- updated `init.sh`, `scripts/verify-fast.sh`, `scripts/cleanup-scanner.sh`, and `scripts/harness-benchmark.sh` to read required files from the manifest
- made `scripts/cleanup-scanner.sh` accept the reset progress template and common no-active-feature handoff wording
- made runtime agent-output checks ignore whitespace-only files

Validation:

- `bash -n init.sh && bash -n scripts/verify-fast.sh && bash -n scripts/cleanup-scanner.sh && bash -n scripts/harness-benchmark.sh`
- `git diff --check`
- `bash scripts/verify-fast.sh`
- `./init.sh`

Next feature:

- `ST-04` Show sign-in failure clearly

### 2026-06-26 — ST-03 Sign in

Completed:

- added sign-in support to the auth domain, data layer, and app navigation flow
- implemented secure persisted session storage plus splash-based session restore
- added sign-in and splash presentation flows with auth-focused tests
- hardened session restore so unreadable or malformed persisted session data is cleared and treated as signed out
- completed reviewer and evaluator workflow stages, then accepted the remaining low-risk placeholder copy as-is

Validation:

- `JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew :domain:auth:test :data:auth:test :feature:auth:test`
- `./init.sh`

Accepted residual risk:

- manual backend or emulator smoke testing was not rerun for sign-in happy path or session restoration
- app-level assemble and a fresh full `./init.sh` were not rerun after the final session-restore hardening fix
- the authenticated placeholder copy still overstates what is available after sign-in, and that wording was explicitly accepted as temporary scaffolding

Next feature:

- `ST-04` Show sign-in failure clearly

### 2026-06-25 — Harness Ownership Cleanup

Completed:

- slimmed `AGENTS.MD` so it stays focused on startup, rules, and ownership boundaries
- rebuilt `.harness/docs/SETUP.MD` as bootstrap and tooling guidance only
- trimmed `.harness/docs/DECISIONS.MD` back to durable project and harness decisions
- added `docs/RELIABILITY.MD` for lifecycle, benchmark, cleanup, and clean-state expectations
- added `.harness/docs/clean-state-checklist.md`
- added `scripts/harness-benchmark.sh` and `scripts/cleanup-scanner.sh`
- strengthened `init.sh` to validate harness files and report the active or next feature dynamically

Moved out of decisions and into archive:

- `ST-01` intentionally ended at registration success plus navigation to a sign-in placeholder because sign-in itself belongs to `ST-03`
- registration implementation details remain tracked through `feature_list.json` evidence plus the `2026-06-19 — Android Scaffold + ST-01 Registration` archive entry below

Validation:

- harness ownership paths updated to reference `docs/RELIABILITY.MD`, `.harness/docs/clean-state-checklist.md`, and the new lifecycle scripts
- `feature_list.json` intentionally kept lean and unchanged
- no feature implementation state was moved out of the archive into startup docs

### 2026-06-25 — Harness Folder Consolidation

Changes:

- moved harness role prompts into `.harness/agents/`
- moved harness operational docs from `docs/` to `.harness/docs/`
- moved runtime handoff output documentation into `.harness/agent_outputs/`
- kept `AGENTS.MD` in the repository root as the main operating entry point
- updated harness and repo references to use the new `.harness/` paths
- kept product and architecture docs in `docs/`

Validation:

- path reference scan completed for old harness locations
- `git diff --check` completed successfully

### 2026-06-24 — Agent Runtime Output Files

Completed:

- added `.harness/agent_outputs/README.MD` to document runtime handoff files
- ignored per-agent runtime outputs in `.gitignore`
- updated Planner, Implementer, Reviewer, and Evaluator prompts to write stage outputs to `.harness/agent_outputs/*.md`
- required each role to display its output file contents before asking for Human-in-the-Loop approval
- documented that runtime output files should be cleared after a feature or harness task is complete

Validation passed:

- `git diff --check`

### 2026-06-24 — Human-In-The-Loop Agent Handoffs

Completed:

- added Human In The Loop sections to Planner, Implementer, Reviewer, and Evaluator prompts
- required explicit human approval before Planner -> Implementer, Implementer -> Reviewer, and Reviewer -> Evaluator handoffs
- required Evaluator to present final verification results and ask for human approval of the next action
- updated `.harness/docs/AGENT_WORKFLOW.MD`, setup, and decision docs with the approval-gate rule

Validation passed:

- `git diff --check`

### 2026-06-23 — Reviewer Role Artifact

Completed:

- added `.harness/agents/REVIEWER.MD` for code quality, architecture, maintainability, and test-quality review
- updated the agent workflow from three roles to Planner, Implementer, Reviewer, and Evaluator
- kept Evaluator as the final completion and evidence judge
- wired Reviewer into setup, decisions, handoff, and agent docs

Validation passed:

- `git diff --check`

### 2026-06-23 — Multi-Agent Role Artifacts

Completed:

- added `.harness/agents/` role prompts for Planner, Implementer, Reviewer, and Evaluator
- added `.harness/docs/AGENT_WORKFLOW.MD` as the operating guide for when and how to use the roles
- updated harness docs to use Implementer instead of the older Generator wording
- wired the new agent artifacts into setup, decisions, and handoff docs

Validation passed:

- `git diff --check`

### 2026-06-23 — Requirements File Relocation

Completed:

- created root `requirements/` folder in the Android repository
- moved Android plan, Android technical design, MVP requirements, and backend business baseline files from the parent ZY-Commerce project into `requirements/`
- removed duplicate `docs/` copies for the plan and MVP requirements after confirming they matched the parent originals
- updated Android repo references to use the new `requirements/` paths

Validation passed:

- `git diff --check`

### 2026-06-23 — Backend Script Relocation

Completed:

- created root `scripts/` folder in the Android repository
- moved backend helper scripts from the parent ZY-Commerce project into `scripts/`
- updated local backend docs to use the new script paths
- updated `scripts/backend-startup.sh` so it calls `docker-start.sh` from its own folder

Validation passed:

- `git diff --check`

### 2026-06-22 — Testing Strategy Harness Consolidation

Completed:

- added `.harness/docs/TESTING_STRATEGY.MD` as the source of truth for test coverage expectations and test-level ownership
- simplified testing guidance in `docs/ARCHITECTURE.MD` to point to the new strategy file
- simplified `.harness/docs/VERIFICATION.MD` so it focuses on commands, evidence, and failure reporting
- updated startup, setup, decisions, and handoff docs to include the testing strategy file

Validation passed:

- `git diff --check`

### 2026-06-22 — Backend Installation Guide Move

Completed:

- moved `backend-installation-instructions.md` from the parent ZY-Commerce project into `.harness/docs/backend-installation-instructions.md`
- updated backend local harness references to use the new docs path
- kept backend runtime script references pointing to the parent project scripts

Validation passed:

- `git diff --check`

### 2026-06-22 — Backend Local Harness Consolidation

Completed:

- added `.harness/docs/BACKEND_LOCAL.MD` as the local backend and Android connection guide
- moved backend startup commands, local URLs, emulator/device rules, debug cleartext notes, and `ST-01` backend smoke prerequisites out of `.harness/docs/SETUP.MD` and `.harness/docs/VERIFICATION.MD`
- updated `AGENTS.MD` startup guidance to read `.harness/docs/BACKEND_LOCAL.MD`
- updated `.harness/docs/DECISIONS.MD` so backend decisions point to `.harness/docs/BACKEND_LOCAL.MD`

Validation passed:

- `git diff --check`

### 2026-06-22 — Verification Harness Consolidation

Completed:

- added `.harness/docs/VERIFICATION.MD` as the operational verification guide
- moved command-level verification details out of `AGENTS.MD` and `.harness/docs/SETUP.MD`
- updated `.harness/docs/DECISIONS.MD` so verification decisions point to `.harness/docs/VERIFICATION.MD`
- updated startup guidance to read `.harness/docs/VERIFICATION.MD` before running checks

Validation passed:

- `git diff --check`

### 2026-06-19 — Android Scaffold + ST-01 Registration

Completed:

- created Android Gradle scaffold and checked in Gradle wrapper
- created `app` module and architecture-aligned modules:
  - `core/common`
  - `core/designsystem`
  - `core/network`
  - `core/database`
  - `core/storage`
  - `domain/auth`
  - `domain/catalog`
  - `data/auth`
  - `data/catalog`
  - `feature/auth`
  - `feature/catalog`
  - `feature/productadmin`
- implemented `ST-01` Register account through:
  - `RegisterScreen`
  - `RegisterViewModel`
  - `RegisterUserUseCase`
  - `AuthRepository`
  - Retrofit `AuthApi`
  - register request/response DTOs and mapper
  - Hilt app/network/auth wiring
- added registration-focused tests for:
  - email trimming and required field validation in domain use case
  - response DTO to domain mapping
  - ViewModel success confirmation state
- added `init.sh` as the standard bootstrap and verification script
- updated `feature_list.json` to mark `ST-01` done with verification evidence
- added `.harness/docs/DECISIONS.MD` to consolidate durable harness and project decisions

Verification passed:

- `JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew test`
- `JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew lintDebug`
- `JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home ./gradlew assembleDebug`

Notes:

- `ST-02` duplicate-email-specific handling remains planned.
- `ST-03` sign-in remains planned; the app currently navigates to a sign-in placeholder after registration success.
- Manual backend/emulator registration smoke testing has not been performed in this session.

### 2026-06-19

Current focus across the latest harness sessions:

- establish a durable harness surface for `zy-commerce-android` before Android implementation begins

Completed so far:

- reviewed the Android repo docs as the startup surface:
  - `docs/PRODUCT.MD`
  - `docs/ARCHITECTURE.MD`
  - `requirements/mvp_requirements_ecommerce.md`
  - `requirements/ecommerce-android-plan.md`
  - `feature_list.json`
- reviewed Harness Engineering repo guidance and examples for:
  - startup workflow
  - feature-driven scope control
  - externalized completion judgment
  - planner / generator / evaluator upgrade path
- created `AGENTS.MD` for the Android repository
- created `.harness/docs/PROGRESS.MD`
- created `.harness/docs/SESSION_HANDOFF.MD`
- created `.harness/docs/SETUP.MD`
- updated `AGENTS.MD`, `PROGRESS.MD`, and `SESSION_HANDOFF.MD` so `.harness/docs/SETUP.MD` is the current bootstrap path

What the harness now defines:

- startup reading order
- docs hierarchy
- one-feature-at-a-time execution rule
- architecture boundary rules
- required artifacts
- three-layer verification philosophy
- definition of done
- future multi-agent role split guidance
- current bootstrap path through `.harness/docs/SETUP.MD`

Repository state after this session:

- harness foundation now exists in the Android repo:
  - `AGENTS.MD`
  - `docs/PRODUCT.MD`
  - `docs/ARCHITECTURE.MD`
  - `requirements/mvp_requirements_ecommerce.md`
  - `requirements/ecommerce-android-plan.md`
  - `feature_list.json`
  - `.harness/docs/SETUP.MD`
  - `.harness/docs/PROGRESS.MD`
  - `.harness/docs/SESSION_HANDOFF.MD`

Open follow-up items:

- keep `.harness/docs/PROGRESS.MD`, `.harness/docs/SESSION_HANDOFF.MD`, `.harness/docs/SETUP.MD`, and `AGENTS.MD` aligned as startup assumptions evolve
- create `init.sh` once the Android project scaffold exists
- strengthen root-level `feature_list.json` with execution evidence when implementation begins
- eventually add evaluator-oriented artifacts when implementation starts

Current implementation status:

- documentation and harness setup only
- no Android app scaffold or feature code has been created yet

Risks / notes:

- `.harness/docs/SETUP.MD` is now the current bootstrap path until a real `init.sh` exists
- current harness assumes any authenticated user can manage products because backend roles do not yet exist
- current repo still lacks executable verification because the Android scaffold and Gradle wrapper do not exist yet
