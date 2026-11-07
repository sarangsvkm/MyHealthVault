import type { ExpoConfig } from "expo/config";

const config: ExpoConfig = {
  name: "MyHealthVault",
  slug: "myhealthvault",
  scheme: "myhealthvault",
  version: "1.0.0",
  orientation: "portrait",
  userInterfaceStyle: "automatic",
  ios: {
    bundleIdentifier: "in.myhealthvault.app",
    supportsTablet: true,
    infoPlist: {
      NSHealthShareUsageDescription:
        "MyHealthVault reads health data you choose to import into your personal health history.",
      NSHealthUpdateUsageDescription:
        "MyHealthVault writes health data only when you explicitly choose to save it."
    },
    entitlements: { "com.apple.developer.healthkit": true }
  },
  android: {
    package: "in.myhealthvault.app",
    permissions: [
      "android.permission.health.READ_STEPS",
      "android.permission.health.READ_HEART_RATE",
      "android.permission.health.READ_WEIGHT",
      "android.permission.health.READ_HEIGHT",
      "android.permission.health.READ_BLOOD_PRESSURE",
      "android.permission.health.READ_BLOOD_GLUCOSE",
      "android.permission.health.READ_SLEEP"
    ]
  },
  plugins: [
    "expo-router",
    "expo-dev-client",
    "expo-secure-store",
    "expo-document-picker",
    "expo-image-picker",
    "expo-notifications"
  ],
  experiments: { typedRoutes: true }
};

export default config;
