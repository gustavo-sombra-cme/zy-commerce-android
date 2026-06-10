# Clean-State Checklist — ZY-Commerce Android

All 5 conditions must pass before ending a session.

## Gate 1 — Build Passes
```bash
./gradlew assembleDebug
```
- [ ] Exit code 0, 0 errors

## Gate 2 — Tests Pass
```bash
./gradlew test
```
- [ ] All unit tests green

## Gate 3 — Feature State Recorded
```bash
cat docs/feature_list.json | jq '.features[] | {id, state}'
```
- [ ] Active feature has meaningful progress or is `passing`
- [ ] No feature stuck in `active` with 0 code changes

## Gate 4 — No Stray Artifacts
- [ ] No `catalog.db` committed (backend local db)
- [ ] No `*.log`, `build/`, `.gradle/` staged
- [ ] No API keys or credentials in any tracked file

## Gate 5 — Startup Path Verified
```bash
./gradlew assembleDebug && echo "OK"
```
- [ ] App installs and reaches product list screen on emulator
