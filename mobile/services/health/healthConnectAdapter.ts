import { Platform } from "react-native";
import { HealthAdapter, HealthRecord } from "./types";

// Health Connect package APIs evolve. Keep this adapter isolated and bind
// its calls to the exact installed react-native-health-connect typings.
// Never treat a successful permission prompt as permission for every type.
export const healthConnectAdapter: HealthAdapter = {
  available: async () => Platform.OS === "android",
  requestPermissions: async () => {
    if (Platform.OS !== "android") return false;
    const hc = await import("react-native-health-connect");
    const ready = await hc.initialize();
    if (!ready) return false;
    const granted = await hc.requestPermission([
      { accessType: "read", recordType: "Steps" },
      { accessType: "read", recordType: "HeartRate" },
      { accessType: "read", recordType: "Weight" },
      { accessType: "read", recordType: "Height" },
      { accessType: "read", recordType: "BloodPressure" },
      { accessType: "read", recordType: "BloodGlucose" },
      { accessType: "read", recordType: "SleepSession" }
    ]);
    return Array.isArray(granted) && granted.length > 0;
  },
  read: async (_from: Date, _to: Date): Promise<HealthRecord[]> => {
    if (Platform.OS !== "android") throw new Error("Health Connect is only available on Android");
    throw new Error("Implement paginated readRecords queries using the installed Health Connect SDK typings and test on a device.");
  }
};
