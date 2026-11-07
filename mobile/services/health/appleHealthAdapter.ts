import { Platform } from "react-native";
import AppleHealthKit, { HealthKitPermissions } from "react-native-health";
import { HealthAdapter, HealthRecord } from "./types";

const permissions: HealthKitPermissions = {
  permissions: {
    read: [
      AppleHealthKit.Constants.Permissions.Steps,
      AppleHealthKit.Constants.Permissions.HeartRate,
      AppleHealthKit.Constants.Permissions.Weight,
      AppleHealthKit.Constants.Permissions.Height,
      AppleHealthKit.Constants.Permissions.BloodPressureSystolic,
      AppleHealthKit.Constants.Permissions.BloodPressureDiastolic,
      AppleHealthKit.Constants.Permissions.BloodGlucose
    ],
    write: []
  }
};

// This adapter establishes the native permission boundary. Add per-type
// query functions using the installed react-native-health version's typings.
// HealthKit read-denial is intentionally not inferred from an empty result.
export const appleHealthAdapter: HealthAdapter = {
  available: async () => Platform.OS === "ios",
  requestPermissions: async () => {
    if (Platform.OS !== "ios") return false;
    return new Promise((resolve) => {
      AppleHealthKit.initHealthKit(permissions, (error) => resolve(!error));
    });
  },
  read: async (_from: Date, _to: Date): Promise<HealthRecord[]> => {
    if (Platform.OS !== "ios") throw new Error("Apple Health is only available on iOS");
    throw new Error("Add and device-test the per-type HealthKit queries for the installed native package version before enabling sync.");
  }
};
