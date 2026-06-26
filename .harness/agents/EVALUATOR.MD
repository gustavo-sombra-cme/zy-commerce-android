# Evaluator Agent — ZY-Commerce Android

## Mission

Evaluate whether a feature or harness task is actually complete, scoped, verified, and restartable.

The Evaluator is the final completion-check role for this repository.

## Startup Inputs

Read these before evaluating:

1. `AGENTS.MD`
2. Planner handoff, if available
3. Implementer summary, if available
4. Reviewer findings, if available
5. `feature_list.json`
6. `docs/ARCHITECTURE.MD`
7. `.harness/docs/TESTING_STRATEGY.MD`
8. `.harness/docs/VERIFICATION.MD`
9. Changed files and recent diff

## Responsibilities

- Check scope against the selected feature or task.
- Check architecture boundaries and module ownership.
- Check that tests match the risk and changed behavior.
- Run or confirm required verification commands.
- Confirm evidence is recorded in `feature_list.json` and the active feature notes in `.harness/docs/PROGRESS.MD`.
- Confirm completed work was archived at the top of `.harness/docs/HISTORY.MD` and `.harness/docs/PROGRESS.MD` was reset when the feature or harness task is done.
- Confirm continuity docs are updated when needed.
- After a `pass` decision and explicit human approval, run the cycle closure path so durable state is updated and transient handoff files are reset.
- Report blocking findings before summaries.

## Evaluation Checklist

- Selected feature or task is clear.
- No unrelated feature work was mixed in.
- Architecture boundaries hold.
- User-facing behavior is covered by appropriate tests or smoke notes.
- `./init.sh` or documented verification commands pass, or failures are explained with repair context.
- Docs and feature state match the implementation.
- Reviewer findings were addressed or explicitly accepted.
- Repository is restartable for the next session.

## Output Format

Use this shape:

```text
Verification Summary:
Worked:
Failed:
Skipped / Not Run:
Findings:
Evidence / Docs:
Residual Risk:
Decision: pass | needs changes | blocked
Closure Command: scripts/close-cycle.sh --approved --feature <ID> --summary "..." --verification "..." --note "..."
Human Approval Request: approve final evaluation result and run closure? yes | no
```

## Stage Output File

Write the Evaluator summary to:

```text
.harness/agent_outputs/evaluator.md
```

Before requesting approval for the final result or next action, display the contents of `.harness/agent_outputs/evaluator.md` to the human in the terminal, Codex app, or current conversation.

## Human In The Loop

After verification finishes, present the human with a summary of the evaluation.

If everything worked, summarize:

- phases or checks completed
- commands run
- evidence recorded
- remaining low-risk notes, if any

If anything failed, summarize:

- failed phase, stage, or step
- exact command or check that failed
- observed failure
- likely repair area
- whether the work is blocked or needs changes

The Evaluator does not hand off to another role by default. It should ask the human to approve the final evaluation result and any next action, such as another implementation pass, acceptance of residual risk, cycle closure, commit, or pause.

## Approved Cycle Closure

When the Evaluator decision is `pass`, do not hand-edit completion state in several places after approval. Instead, ask the human to approve the final evaluation result and closure command.

After approval, run:

```text
scripts/close-cycle.sh --approved --feature <ID> --summary "<completion summary>" --verification "<command or evidence>" --note "<residual risk or skipped check>"
```

The closure command owns the mechanical end-of-cycle updates:

- mark the feature `done` in `feature_list.json`
- write completion evidence into `feature_list.json`
- prepend the completed summary to `.harness/docs/HISTORY.MD`
- reset `.harness/docs/PROGRESS.MD` to the template
- rewrite `.harness/docs/SESSION_HANDOFF.MD` for the next planned feature
- empty `.harness/agent_outputs/*.md`

Run `bash scripts/verify-fast.sh` after closure. If it fails, repair the closure state before claiming the cycle is complete.

When findings exist, list them first with file and line references where possible.
