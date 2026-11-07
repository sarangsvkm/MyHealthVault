# MyHealthVault v1.0 — runnable starter

Personal health records for Android and iOS, with a Spring Boot API and PostgreSQL.

## What is included
- Expo React Native app scaffold with login, dashboard, report upload, health sync controls.
- Spring Boot 3.5 / Java 21 API with JWT authentication, user-scoped health records, report upload/download, sync jobs.
- PostgreSQL + Flyway migrations.
- Docker Compose for local PostgreSQL and API.
- Local/cloud deployment configuration pattern.
- Apple HealthKit and Android Health Connect integration adapters and native setup notes.
- ABDM adapter boundary and sandbox configuration placeholders.

## Important integration boundaries
1. Apple HealthKit and Android Health Connect permissions are granted on-device, not by the backend. The included mobile adapters are a starting point and must be built and tested on physical devices.
2. Expo Go is not supported for these native integrations. Use an Expo development build / EAS Build.
3. ABDM access requires official sandbox credentials, registered callback URLs, consent flows, conformance testing and production approval. This project intentionally does not invent ABDM endpoints or credentials.
4. Automatic sync is best-effort background sync. iOS and Android control background execution; do not promise exact intervals. Manual sync is always available.
5. This starter stores medical files privately on the API server. For production, use encrypted object storage, malware scanning, backups, monitoring and a security review.

## Requirements
- Java 21
- Maven 3.9+
- Docker Desktop
- Node.js LTS and npm
- Expo/EAS CLI
- Android Studio for Android builds
- macOS + Xcode for iOS builds

## 1. Start backend and PostgreSQL
From repository root:

```bash
docker compose -f deployment/docker/docker-compose.yml up --build
```

API: http://localhost:8080  
Health check: http://localhost:8080/actuator/health

For mobile testing on a physical device, set `EXPO_PUBLIC_API_URL` to your computer's LAN IP, e.g. `http://192.168.1.20:8080`. Do not use `localhost` on the phone.

## 2. Start the mobile app
```bash
cd mobile
npm install
npx expo prebuild
npx expo run:android
```

For iOS, run these commands on the Mac:
```bash
cd mobile
npm install
npx expo prebuild
npx pod-install
npx expo run:ios
```

Expo Go will not load HealthKit / Health Connect native modules.

## 3. Configure secrets
Copy `backend/myhealthvault-api/.env.example` to `.env` or export the variables in your shell. Change all secrets before use. Never commit real secrets.

Required:
- `JWT_SECRET` — at least 32 random bytes encoded as a string.
- `APP_STORAGE_DIR` — private directory for medical files.
- `DB_PASSWORD` — strong database password.

## 4. API quick start
Register:
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"demo@example.com","password":"ChangeMe123!","displayName":"Demo"}'
```

Login:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"demo@example.com","password":"ChangeMe123!"}'
```

Use the returned access token as `Authorization: Bearer <token>`.

## 5. Local + cloud deployment
- Local: Docker Compose on a trusted home/office server; expose only through a VPN or a properly configured HTTPS reverse proxy.
- Cloud: deploy the same API container to a cloud VM/container platform and use managed PostgreSQL plus private object storage.
- Do not expose PostgreSQL publicly.
- Use separate database credentials and JWT secrets for local and cloud environments.
- Configure a stable HTTPS API URL for production builds.
- This starter does not implement bidirectional server-to-server replication. The mobile app syncs device records to the selected API environment. If local and cloud must share records, configure one authoritative backend or implement an audited replication service; do not run two writable databases without conflict resolution.

## 6. Native health setup
See `documentation/NATIVE_HEALTH_SETUP.md`.

## 7. ABDM setup
See `documentation/ABDM_SETUP.md`. Until sandbox credentials are supplied, ABDM adapter returns `NOT_CONFIGURED`; it does not fabricate a successful connection.

## 8. Production checklist
- Complete threat model and independent security assessment.
- Enable TLS everywhere.
- Replace local disk storage with encrypted private object storage.
- Add malware scanning and PDF/image content validation.
- Add refresh-token rotation, account recovery and MFA.
- Add audit events and retention/deletion workflows.
- Verify Indian privacy, ABDM, Apple App Store and Google Play requirements with qualified counsel.
- Test backup restoration and disaster recovery.
# MyHealthVault
