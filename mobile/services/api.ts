import * as SecureStore from "expo-secure-store";

const API = process.env.EXPO_PUBLIC_API_URL || "http://localhost:8080";

export async function saveToken(token: string) {
  await SecureStore.setItemAsync("access_token", token);
}
export async function getToken() {
  return SecureStore.getItemAsync("access_token");
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
