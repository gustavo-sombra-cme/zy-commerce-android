# Design Decisions — ZY-Commerce Android

| ID | Decision | Rationale | Alternatives Considered | Date |
|----|----------|-----------|------------------------|------|
| D01 | Jetpack Compose + Material 3 | Modern Android UI toolkit, declarative, Google-recommended | XML layouts | 2026-06-09 |
| D02 | Hilt for DI | Official Android DI, compile-time validation, Compose-integrated | Koin, manual DI | 2026-06-09 |
| D03 | kotlinx-serialization over Gson | Pure Kotlin, null-safe, no reflection issues with R8 | Gson, Moshi | 2026-06-09 |
| D04 | StateFlow + sealed classes for UI state | Predictable state machine, lifecycle-aware, testable | LiveData, SharedFlow | 2026-06-09 |
| D05 | Channel for one-shot events | Avoids StateFlow replaying navigation/snackbar on recompose | SharedFlow, StateFlow | 2026-06-09 |
| D06 | `Result<T>` in Repository | Explicit error handling without exceptions crossing layer boundaries | Sealed classes, Either | 2026-06-09 |
| D07 | 10.0.2.2 for backend URL in debug | Android emulator loopback to host machine localhost | ADB port forward | 2026-06-09 |
| D08 | BuildConfig.BASE_URL | URL configurable per build type without touching source code | Hardcoded string | 2026-06-09 |
