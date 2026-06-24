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

Each role prompt lives in `agents/`:

- `agents/PLANNER.MD`
- `agents/IMPLEMENTER.MD`
- `agents/REVIEWER.MD`
- `agents/EVALUATOR.MD`

## Planner Handoff Requirements

The Planner should provide:

- selected feature or task
- explicit scope and out-of-scope items
- relevant files and modules
- implementation notes
- verification plan
- documentation updates
- risks or user questions

## Implementer Handoff Requirements

The Implementer should provide:

- files changed
- behavior implemented
- tests or verification run
- evidence updated
- known gaps
- anything the Reviewer or Evaluator should inspect closely

## Reviewer Review Requirements

The Reviewer should provide:

- findings with file and line references where possible
- architecture and maintainability notes
- test-quality concerns
- security or privacy concerns
- suggested fixes before final evaluation
- whether the change is ready for Evaluator review

## Evaluator Completion Requirements

The Evaluator should consider Reviewer findings before passing work. The Evaluator should not pass work unless:

- scope matches the selected feature or task
- architecture boundaries hold
- relevant tests or verification ran
- evidence is recorded
- docs match the current behavior
- the repo can be restarted from `AGENTS.MD` and `docs/SETUP.MD`

## Evidence

Feature completion evidence belongs in `feature_list.json` and/or `docs/PROGRESS.MD`.

Operational verification commands and failure reporting live in `docs/VERIFICATION.MD`.
