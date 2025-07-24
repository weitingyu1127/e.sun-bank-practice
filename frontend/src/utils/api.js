const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export async function callApi(path, method = 'GET', body = null) {
  try {
    const res = await fetch(`${BASE_URL}${path}`, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: body ? JSON.stringify(body) : null
    });

    const text = await res.text();
    return { success: res.ok, message: text };
  } catch (e) {
    return { success: false, message: '連線失敗' };
  }
}
