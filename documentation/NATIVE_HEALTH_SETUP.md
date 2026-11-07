# Native health integration setup

## Expo workflow
This app requires a custom native development build. Expo Go cannot load the native HealthKit / Health Connect modules.

```bash
cd mobile
npm install
npx expo prebuild --clean
```

Run Android from Windows:
```bash
npx expo run:android
```

Run iOS from macOS:
```bash
npx pod-install
npx expo run:ios
```

## iOS HealthKit
1. Open the generated `ios/*.xcworkspace` in Xcode.
2. Select the app target → Signing & Capabilities.
3. Add HealthKit capability.
4. Confirm the HealthKit entitlement is present.
5. Keep the read purpose string in `app.config.ts`.
6. Test permission prompts and each query on a real iPhone.
7. Implement read queries in `appleHealthAdapter.ts` against the installed `react-native-health` package typings. Request only types actually used.
8. HealthKit privacy does not reveal whether read permission was denied; an empty query result is not proof of denial.

## Android Health Connect
1. Install Health Connect on the Android device if the OS version requires a separate app.
2. Confirm manifest permissions generated from `app.config.ts` match the record types used.
3. Add the official Health Connect rationale/privacy policy flow and required Play Console declarations.
4. Implement paginated `readRecords` queries in `healthConnectAdapter.ts` using the installed package version's TypeScript definitions.
5. Test permission revocation, empty results, historical access and data deletion.
6. Do not request `READ_HEALTH_DATA_HISTORY` unless the app genuinely needs data older than the standard history window and the permission is approved.

## Background sync
Background execution is controlled by iOS and Android. Use it as a best-effort refresh, not a guaranteed schedule. Always provide a manual Sync Now action and show last successful sync time.
