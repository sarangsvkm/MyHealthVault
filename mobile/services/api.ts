import * as SecureStore from "expo-secure-store";
import { Platform } from "react-native";

const API = process.env.EXPO_PUBLIC_API_URL || "http://localhost:8080";

export async function saveToken(token: string) {
  if (Platform.OS === "web" || typeof localStorage !== "undefined") {
    try { localStorage.setItem("access_token", token); } catch {}
  }
  try {
    await SecureStore.setItemAsync("access_token", token);
  } catch {}
}

export async function getToken() {
  try {
    const t = await SecureStore.getItemAsync("access_token");
    if (t) return t;
  } catch {}
  if (typeof localStorage !== "undefined") {
    return localStorage.getItem("access_token");
  }
  return null;
}
export async function api(path: string, options: RequestInit = {}) {
  const token = await getToken();
  const headers = new Headers(options.headers || {});
  if (token) headers.set("Authorization", `Bearer ${token}`);
  if (options.body && !(options.body instanceof FormData)) headers.set("Content-Type","application/json");
  const response = await fetch(`${API}${path}`, {...options, headers});
  if (!response.ok) throw new Error(`API ${response.status}: ${await response.text()}`);
  return response.status === 204 ? null : response.json();
}
export { API };
