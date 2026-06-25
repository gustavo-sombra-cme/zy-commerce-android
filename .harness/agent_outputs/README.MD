# Agent Outputs

This folder is for runtime handoff files created by the multi-agent workflow.

Tracked file:

- `README.MD`: explains the folder.

Ignored runtime files:

- `planner.md`
- `implementer.md`
- `reviewer.md`
- `evaluator.md`

Each role writes its Human-in-the-Loop approval summary to its own file and then presents that file's contents to the human before the next role starts.

When a feature or harness task is finished, clear these runtime files by deleting them or truncating their contents. They are intentionally ignored by git so handoff drafts do not become committed noise.
