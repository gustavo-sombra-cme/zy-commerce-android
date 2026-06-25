# AGENT_WORKFLOW.MD — ZY-Commerce Android

This file defines when and how to use the repository's four-agent workflow.

## Canonical Roles

Use these names in this repository:

- Planner
- Implementer
- Reviewer
- Evaluator

Historical harness wording may say Generator or Checker. In this Android repo:

- Implementer replaces Generator.
- Reviewer is the code-quality review role.
- Evaluator is the final completion-check role.

## When To Use Multiple Agents

Use a single agent for small, low-risk changes.

Use Planner, Implementer, Reviewer, and Evaluator when work has any of these traits:

- auth/session behavior
- catalog search, filter, paging, or cache behavior
- product create, update, or deactivate flows
- broad module or architecture changes
- backend contract uncertainty
- failing baseline verification
- unclear story scope or dependency risk
- changes that need manual smoke evidence
- changes where code quality, maintainability, or architecture judgment should be reviewed separately

## Workflow

1. Planner defines scope, dependencies, files, risks, and verification intent.
2. Implementer makes the scoped code and documentation changes.
3. Reviewer checks code quality, maintainability, architecture practices, and regression risk.
4. Evaluator checks scope, evidence, verification, docs, and restartability.

Each role prompt lives in `.harness/agents/`:

- `.harness/agents/PLANNER.MD`
- `.harness/agents/IMPLEMENTER.MD`
- `.harness/agents/REVIEWER.MD`
- `.harness/agents/EVALUATOR.MD`

## Runtime Output Files

Each role must write its stage output to a runtime file before asking for Human-in-the-Loop approval:

- Planner: `.harness/agent_outputs/planner.md`
- Implementer: `.harness/agent_outputs/implementer.md`
- Reviewer: `.harness/agent_outputs/reviewer.md`
- Evaluator: `.harness/agent_outputs/evaluator.md`

The role must display the file contents to the human in the terminal, Codex app, or current conversation before asking for approval.

These files are ignored by git. When a feature or harness task is finished, delete them or clear their contents so the next feature starts with empty handoff state.

## Human In The Loop

Every role handoff requires explicit human approval before the next role begins. Do not assume approval from silence or from a successful previous step.

Required approval gates:

- Planner -> Implementer: human approves the plan summary and details.
- Implementer -> Reviewer: human approves the implementation summary and change details.
- Reviewer -> Evaluator: human approves the review result after issues are addressed or accepted.
- Evaluator -> Next action: human approves the final evaluation result and any next step.

## Planner Handoff Requirements

The Planner should provide:

- selected feature or task
- explicit scope and out-of-scope items
- relevant files and modules
- implementation notes
- verification plan
- documentation updates
- risks or user questions
- explicit human approval request to continue to Implementer

## Implementer Handoff Requirements

The Implementer should provide:

- files changed
- behavior implemented
- tests or verification run
- evidence updated
- known gaps
- anything the Reviewer or Evaluator should inspect closely
- explicit human approval request to continue to Reviewer

## Reviewer Review Requirements

The Reviewer should provide:

- findings with file and line references where possible
- architecture and maintainability notes
- test-quality concerns
- security or privacy concerns
- suggested fixes before final evaluation
- findings grouped by Critical, High, Medium, Low, and Info
- whether issues were addressed or explicitly accepted
- whether the change is ready for Evaluator review
- explicit human approval request to continue to Evaluator

## Evaluator Completion Requirements

The Evaluator should consider Reviewer findings before passing work. The Evaluator should not pass work unless:

- scope matches the selected feature or task
- architecture boundaries hold
- relevant tests or verification ran
- evidence is recorded
- docs match the current behavior
- the repo can be restarted from `AGENTS.MD` and `.harness/docs/SETUP.MD`
- final evaluation summary was presented to the human

## Evidence

Feature completion evidence belongs in `feature_list.json` and/or `.harness/docs/PROGRESS.MD`.

Operational verification commands and failure reporting live in `.harness/docs/VERIFICATION.MD`.
