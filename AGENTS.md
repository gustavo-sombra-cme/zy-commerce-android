# ZY-Commerce Android — AGENTS.md

> **Entry file** · 50-200 lines · Router only. Add details to docs/, not here.

## Project Overview
Android app for the ZY-Commerce e-commerce platform.
Consumes the ZY-Commerce backend REST API (running at `http://10.0.2.2:5015` from emulator).

**Stack:** Kotlin 2.0 · Jetpack Compose + Material 3 · Hilt · Retrofit + kotlinx-serialization · MVVM · Navigation Compose  
**Min SDK:** 24 (Android 7.0) · **Target SDK:** 35 (Android 15)

## Quick Start
```bash
# 1. Start the backend first (see backend repo)
# 2. Open in Android Studio or build via CLI:
./gradlew assembleDebug
./gradlew test
```

## Hard Constraints (Never Break)
1. **One active feature at a time** — see `docs/feature_list.json`
2. **AGENTS.md stays 50-200 lines** — link to docs for detail
3. **No secrets in source** — use BuildConfig fields, never hardcode URLs in prod
4. **Architecture layers respected**: UI → ViewModel → UseCase → Repository → API
5. **No direct API calls from Composables** — only via ViewModel
6. **ViewModel has no Android framework imports** (except `SavedStateHandle`)
7. **All network calls are suspend functions** — no blocking calls on main thread

## Work Rules
- **WIP = 1**: Only one feature `active` in `feature_list.json` at a time
- **State transitions**: Only `verify.sh` moves a feature to `passing`
- **Session clock-in**: Read `PROGRESS.md` before touching code
- **Session clock-out**: Update `PROGRESS.md`, run `./gradlew test`, commit

## Architecture at a Glance
```
UI (Compose Screens)
  ↓ observes StateFlow
ViewModel (state + events)
  ↓ calls
UseCases (domain logic)
  ↓ calls
Repository (interface)
  ↓ implemented by
RepositoryImpl (data layer)
  ↓ calls
ApiService (Retrofit)
  ↓ HTTP
Backend API (http://10.0.2.2:5015)
```

## Topic Docs
| Topic | File |
|-------|------|
| Architecture | `docs/ARCHITECTURE.md` |
| Feature list | `docs/feature_list.json` |
| API patterns | `docs/api-patterns.md` |
| Testing standards | `docs/testing-standards.md` |
| Sprint contract | `.harness/sprint-contract.md` |
| Quality rubric | `.harness/evaluator-rubric.md` |
| Session handoff | `.harness/session-handoff.md` |
| Exit checklist | `.harness/clean-state-checklist.md` |

## Session Lifecycle
**Clock-in:** `cat PROGRESS.md` → `cat docs/feature_list.json`  
**Clock-out:** Update `PROGRESS.md` → `./gradlew test` → `.harness/verify.sh` → commit
