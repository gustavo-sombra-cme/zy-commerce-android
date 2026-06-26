# Clean State Checklist

Run this checklist before ending a risky session, before committing harness changes, and after finishing a feature.

If an item is not yet applicable because the subsystem is not implemented, record that explicitly in `feature_list.json` or `.harness/docs/PROGRESS.MD` instead of assuming pass.

## Startup

- [ ] `./init.sh` passes
- [ ] startup output reports the correct active or next feature
- [ ] required harness docs exist

## Verification

- [ ] required Gradle checks or documented feature-specific checks ran
- [ ] `feature_list.json` contains current evidence for any completed feature work
- [ ] skipped checks are recorded in `feature_list.json` or `.harness/docs/PROGRESS.MD`

## Continuity

- [ ] `.harness/docs/PROGRESS.MD` reflects only the active feature or is reset to the template
- [ ] `.harness/docs/HISTORY.MD` contains completed summaries only
- [ ] `.harness/docs/SESSION_HANDOFF.MD` is short and restart-oriented

## Harness Hygiene

- [ ] `docs/RELIABILITY.MD`, `.harness/docs/VERIFICATION.MD`, and `.harness/docs/TESTING_STRATEGY.MD` still agree on their responsibilities
- [ ] `AGENTS.MD` still reflects the current startup path and ownership model
- [ ] `bash scripts/cleanup-scanner.sh` reports no blocking issues

## Storage And Session State

- [ ] Room cache expectations are still accurate for the current feature scope
- [ ] DataStore or session-storage expectations are documented accurately for the current implementation state
- [ ] token persistence, clearing, or expiration behavior is either verified, explicitly skipped, or marked not yet implemented
- [ ] no feature was marked complete while session persistence behavior remained unspoken

## Networking And Debug Configuration

- [ ] backend availability was confirmed or the smoke path was intentionally skipped and recorded
- [ ] emulator or device base URL assumptions still match `.harness/docs/BACKEND_LOCAL.MD`
- [ ] debug cleartext assumptions remain local-only and debug-only
- [ ] no release or non-local behavior was accidentally documented as using local HTTP

## Protected Routing And UX Expectations

- [ ] protected actions either route signed-out users correctly, remain out of scope, or are explicitly marked not yet implemented
- [ ] user-facing auth or validation behavior that changed has corresponding smoke evidence or an explicit skip note
- [ ] no silent failure path was accepted for protected behavior

## Smoke Evidence

- [ ] smoke notes say whether testing used emulator, physical device, or no device run
- [ ] smoke notes say whether the backend was reachable
- [ ] smoke notes say what user-visible behavior was exercised
- [ ] smoke notes say what was intentionally skipped

## Repository State

- [ ] no stale runtime handoff files are left under `.harness/agent_outputs/`
- [ ] no generated or temporary noise is being treated as durable repository state
