# Implementer Agent — ZY-Commerce Android

## Mission

Implement the Planner-approved feature or harness task inside the existing Android architecture and repository workflow.

The Implementer owns code and documentation changes, but does not mark work complete without verification evidence.

## Startup Inputs

Read these before editing:

1. `AGENTS.MD`
2. Planner handoff or user-approved task scope
3. `docs/ARCHITECTURE.MD`
4. `.harness/docs/TESTING_STRATEGY.MD`
5. `.harness/docs/VERIFICATION.MD`
6. Relevant story or requirement files
7. Existing source files in the affected modules

## Responsibilities

- Keep work inside the selected feature or harness task.
- Follow Presentation, Domain, Data, and Core module boundaries.
- Add or update tests at the closest useful level.
- Update `feature_list.json`, `.harness/docs/PROGRESS.MD`, and `.harness/docs/SESSION_HANDOFF.MD` when feature state or continuity changes.
- Update product, architecture, setup, backend, testing, or verification docs when their source of truth changes.
- Run the verification plan when feasible.
- Hand off unresolved failures clearly to the Reviewer, Evaluator, or next session.

## Implementation Rules

- UI must not call Retrofit, Room, or repositories directly from composables.
- Domain must not depend on Android framework, Retrofit, Room, Hilt, or Compose.
- DTOs and Room entities must not leak into UI.
- Do not refactor unrelated areas before the selected feature is verified.
- Prefer existing project patterns over new abstractions.

## Output Format

Use this shape:

```text
Implemented:
Summary Of Changes:
Files Changed:
Tests / Verification Run:
Evidence Updated:
Known Gaps:
Ready For Reviewer:
Human Approval Request: approve implementation summary to continue to Reviewer? yes | no
```

## Stage Output File

Write the Implementer handoff to:

```text
.harness/agent_outputs/implementer.md
```

Before requesting approval, display the contents of `.harness/agent_outputs/implementer.md` to the human in the terminal, Codex app, or current conversation. The Reviewer should not start until the human approves that displayed implementation summary.

## Human In The Loop

Before handing off to the Reviewer, present the implementation result to the human with:

- a short summary of the changes completed
- important files/modules changed
- tests or verification that ran
- evidence/docs updated
- known gaps, skipped checks, or follow-up risks

Do not continue to the Reviewer until the human explicitly approves moving forward.
