export type Provider = "APPLE_HEALTH" | "HEALTH_CONNECT";
export type HealthRecord = {
  provider: Provider;
  type: "STEPS"|"HEART_RATE"|"WEIGHT"|"HEIGHT"|"BLOOD_PRESSURE_SYSTOLIC"|"BLOOD_PRESSURE_DIASTOLIC"|"BLOOD_GLUCOSE"|"SLEEP_MINUTES";
  value: number;
  unit: string;
  startTime: string;
  endTime?: string;
  sourceRecordId?: string;
};
export interface HealthAdapter {
  available(): Promise<boolean>;
  requestPermissions(): Promise<boolean>;
  read(from: Date, to: Date): Promise<HealthRecord[]>;
}
