# TESTING_STRATEGY.MD — ZY-Commerce Android

This file defines what kinds of tests the Android app should grow and where each kind belongs. Use `.harness/docs/VERIFICATION.MD` for the commands to run and the evidence to record.

## Purpose

Testing should protect feature behavior, architecture boundaries, backend contracts, and user-facing flows without turning every story into a broad regression project.

Use the smallest reliable test level first:

- unit tests for pure rules, ViewModel state, mappers, and error normalization
- integration tests for Retrofit, Room, DataStore, repositories, and session behavior
- Compose UI tests for important screen flows and navigation effects
- manual or scripted smoke checks when backend/device behavior matters
- a small curated regression pack for flows that must not break between stories

## Test Ownership By Layer

Presentation tests should cover:

- ViewModel state transitions
- form validation and submit enablement
- loading, success, empty, and error states
- one-time navigation or snackbar effects
- Compose screen behavior for critical flows

Domain tests should cover:

- use-case validation and orchestration
- feature rules that do not require Android framework code
- repository interface expectations through fakes

Data tests should cover:

- DTO to domain mapping
- backend error normalization
- Retrofit serialization and endpoint contracts
- Room DAO behavior with an in-memory database
- DataStore persistence
- session clearing after `401` responses

App-level smoke checks should cover:

- backend reachability from the selected device target
- register and sign-in happy paths after those stories exist
- public catalog access when signed out
- protected action behavior when signed out
- token/session behavior across app restart after session work exists

## Current Tool Choices

Use the tools already selected by `docs/ARCHITECTURE.MD`:

- JUnit
- MockK
- Truth
- kotlinx-coroutines-test
- Turbine
- MockWebServer
- Room in-memory database support
- Hilt testing
- Compose UI Test

For screenshot regression, prefer Paparazzi or Shot only when stable screens justify the added maintenance cost.

## Story-Level Expectations

Every implemented story should add or update tests at the level closest to the changed behavior.

For the current MVP backlog:

- Auth stories should prioritize use-case validation, ViewModel state, DTO mapping, backend contract coverage, and UI smoke paths.
- Catalog stories should prioritize repository integration, paging/search/filter state, cache behavior, empty/error states, and UI list behavior.
- Product-admin stories should prioritize form validation, protected-action handling, create/update/deactivate repository behavior, and confirmation flows.

A story can be complete without every test level when the missing level is not relevant yet, but the gap must be recorded in `feature_list.json` or `.harness/docs/PROGRESS.MD`.

## Regression Pack

Keep regression focused and intentional. The initial automated regression pack should grow around:

- auth registration and sign-in smoke tests
- session restoration and session clearing behavior
- catalog repository integration tests
- catalog list/search/filter UI behavior
- product create/update/deactivate smoke paths
- screenshot checks for stable high-value screens

Do not add broad regression coverage before the underlying feature is stable enough to keep the tests useful.

## Manual QA Focus

Manual testing is mainly for device, backend, and UX confidence. Before manual backend or emulator smoke testing, use `.harness/docs/BACKEND_LOCAL.MD`.

Current manual QA concerns:

- emulator connects to the local backend
- physical device connects over LAN when needed
- validation and conflict messages are readable
- public catalog works signed out
- protected actions require sign-in
- session survives app restart once session persistence exists
- invalid token clears session safely once token handling exists

## Relationship To Verification

`.harness/docs/TESTING_STRATEGY.MD` answers what kinds of tests should exist.

`.harness/docs/VERIFICATION.MD` answers how to run checks, what evidence to record, and how to report failures.
