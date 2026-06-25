# Agent Roles — ZY-Commerce Android

This folder contains reusable role prompts for multi-agent Android feature work.

Use a single agent by default. Use the four-role flow when feature risk, ambiguity, code quality risk, or blast radius justifies role separation.

Roles:

- `PLANNER.MD`: defines scope, constraints, dependencies, and verification intent before implementation.
- `IMPLEMENTER.MD`: makes the scoped code and documentation changes.
- `REVIEWER.MD`: reviews code quality, architecture practices, maintainability, and regression risk.
- `EVALUATOR.MD`: checks scope completion, verification evidence, docs, and restartability before completion.

Operational workflow lives in `.harness/docs/AGENT_WORKFLOW.MD`.
