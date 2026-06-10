# Progress — ZY-Commerce Android

## Current Snapshot (update each session)
- **Date:** 2026-06-09
- **Active feature:** F01 — Product Catalog UI
- **Last action:** Initial project scaffold created
- **Next action:** Open in Android Studio, run on emulator, verify all 4 API calls work

## Completed
- [x] Android project scaffold (Gradle 8.9, AGP 8.7, Kotlin 2.0)
- [x] Harness structure adapted for Android
- [x] Network layer (Retrofit + kotlinx-serialization + OkHttp)
- [x] Catalog feature: data layer (DTOs, ApiService, Repository)
- [x] Catalog feature: domain layer (models, use cases)
- [x] Catalog feature: presentation layer (ViewModels, Compose screens)
- [x] Navigation (NavHost with product list → detail)
- [x] Unit tests for ProductListViewModel

## In Progress
- [ ] F01: Run on emulator and smoke test all 4 API endpoints

## Blocked
_None_

## Next Steps
1. Open `zy-commerce-android/` in Android Studio
2. Sync Gradle, resolve any dependency issues
3. Start emulator (API 35)
4. Run app → verify product list loads from backend
5. Test create product, view detail, delete product

## Landmines
- Backend runs on `http://localhost:5015` on host → use `10.0.2.2:5015` from emulator
- `network_security_config.xml` allows cleartext to `10.0.2.2` — do NOT remove
- Backend uses SQLite for local dev (macOS workaround) — not for production
- AGP 8.7 requires Java 17+: `export JAVA_HOME=$(brew --prefix openjdk@17)`
