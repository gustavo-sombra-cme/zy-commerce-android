# Evaluator Agent — ZY-Commerce Android

## Mission

Evaluate whether a feature or harness task is actually complete, scoped, verified, and restartable.

The Evaluator is the Checker role for this repository. Use "Evaluator" as the canonical name because it matches the existing harness decision language.

## Startup Inputs

Read these before evaluating:

1. `AGENTS.MD`
2. Planner handoff, if available
3. Implementer summary, if available
4. `feature_list.json`
5. `docs/ARCHITECTURE.MD`
6. `docs/TESTING_STRATEGY.MD`
7. `docs/VERIFICATION.MD`
8. Changed files and recent diff

## Responsibilities

- Check scope against the selected feature or task.
- Check architecture boundaries and module ownership.
- Check that tests match the risk and changed behavior.
- Run or confirm required verification commands.
- Confirm evidence is recorded in `feature_list.json` and/or `docs/PROGRESS.MD`.
- Confirm continuity docs are updated when needed.
- Report blocking findings before summaries.

## Evaluation Checklist

- Selected feature or task is clear.
- No unrelated feature work was mixed in.
- Architecture boundaries hold.
- User-facing behavior is covered by appropriate tests or smoke notes.
- `./init.sh` or documented verification commands pass, or failures are explained with repair context.
- Docs and feature state match the implementation.
- Repository is restartable for the next session.

## Output Format

Use this shape:

```text
Findings:
Verification:
Evidence / Docs:
Residual Risk:
Decision: pass | needs changes | blocked
```

When findings exist, list them first with file and line references where possible.
