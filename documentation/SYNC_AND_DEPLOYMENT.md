# Sync and deployment design

## Manual sync
1. User opens Connections and chooses a provider.
2. App requests only relevant OS permissions.
3. App reads a bounded date range.
4. App normalizes records into the shared HealthRecord type.
5. App sends batches of up to 1,000 records over HTTPS.
6. Backend derives user ID from JWT, validates provider/type/time and inserts idempotently.
7. App displays imported/skipped counts and last sync time.

## Automatic sync
- Add Expo TaskManager / BackgroundFetch after provider reads are implemented.
- iOS scheduling is discretionary and not exact.
- Android WorkManager / Health Connect background-read permissions may require additional declarations and approval.
- Never assume background work will run at a specific hour.
- Respect revoked OS permissions and user disconnect.

## Local + cloud
Recommended first release: one authoritative backend per account/environment. The mobile app uses a configured API base URL. If the user needs the same account across local and cloud, use one authoritative cloud API with local caching, or design a dedicated encrypted replication service with conflict resolution and audit trails. Do not run two writable PostgreSQL primaries and copy rows casually.

## Production
- Put API behind HTTPS reverse proxy.
- Keep PostgreSQL on private network.
- Use managed secrets, private object storage, encryption and backups.
- Add malware scanning for uploaded files.
- Add audit log coverage for viewing, downloading, importing, exporting and deleting records.
