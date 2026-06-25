# BACKEND_LOCAL.MD — ZY-Commerce Android

This file describes how the Android app connects to the local ZY-Commerce backend during development and manual smoke testing.

## Local Backend Role

The Android MVP depends on the existing ZY-Commerce backend for:

- account registration
- sign in
- current user profile
- product catalog reads
- product-management writes

Current local backend targets:

- host API: `http://localhost:5015`
- emulator API: `http://10.0.2.2:5015/`
- Swagger: `http://localhost:5015/swagger/index.html`

## Supporting Backend Scripts

Backend installation guidance now lives in this Android repository:

- `.harness/docs/backend-installation-instructions.md`

Backend runtime scripts live in this Android repository:

- `./scripts/backend-startup.sh`
- `./scripts/backend-API-check.sh`
- `./scripts/docker-start.sh`
- `./scripts/docker-stop.sh`

## Availability Check

Before Android integration or manual smoke testing, confirm the backend is available:

```bash
./scripts/backend-API-check.sh
```

If the API is not running, start it with:

```bash
./scripts/backend-startup.sh
```

Then rerun the API check before testing Android flows.

## Android Emulator Connection

Debug emulator builds use:

```text
http://10.0.2.2:5015/
```

Reason: `10.0.2.2` is the Android emulator route to the host machine's localhost.

## Physical Device Connection

For a physical device on the same Wi-Fi network, use:

```text
http://<mac-local-ip>:5015/
```

Requirements:

- Mac and device are on the same network.
- Backend is listening and reachable from the LAN.
- Android debug build is configured for the selected local base URL.

## Release And Non-Local Environments

Release and non-local environments must not assume local HTTP.

Use environment-specific HTTPS configuration when staging or production environments exist.

## Debug Network Security

The debug manifest allows cleartext traffic only for local development hosts:

- `10.0.2.2`
- `localhost`

Do not add broad release cleartext traffic.

## Registration Smoke Prerequisites

Before manually testing registration:

1. Start or verify the backend.
2. Confirm the Android app is using the emulator base URL for emulator testing.
3. Launch the debug app.
4. Submit a unique email and password from `RegisterScreen`.
5. Confirm the backend returns `userId` and `email`.
6. Confirm the app shows success and routes to the sign-in placeholder.
7. Submit an already-registered email and confirm the app stays on registration, preserves entered values, and shows duplicate-email feedback under the email field.

Known related follow-ups:

- actual sign-in belongs to `ST-03`
- session persistence is not implemented yet
