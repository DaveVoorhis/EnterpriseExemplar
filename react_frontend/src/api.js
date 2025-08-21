import { v4 as uuid } from 'uuid'

const DEFAULT_API_BASE = 'http://localhost:8080/api';

// Read from Vite env or fall back
const API_BASE = import.meta.env.VITE_API_BASE || DEFAULT_API_BASE;

// Token strategy:
// 1) Use VITE_AUTH_TOKEN if provided for local dev
// 2) Otherwise try localStorage 'authToken'
function getAuthToken() {
  return import.meta.env.VITE_AUTH_TOKEN || localStorage.getItem('authToken') || '';
}

// Helper to safely parse JSON (empty body -> null)
async function parseJsonSafe(res) {
  const text = await res.text();
  if (!text) return null;
  try { return JSON.parse(text); } catch { return text; }
}

// Basic timeout wrapper for fetch
function withTimeout(ms, promise) {
  const abort = new AbortController();
  const t = setTimeout(() => abort.abort(), ms);
  return {
    signal: abort.signal,
    finally: () => clearTimeout(t)
  };
}

export async function api(path, options = {}) {
  const url = path.startsWith('http') ? path : `${API_BASE}/${path.replace(/^\//,'')}`;

  const { signal, finally: clearTimer } = withTimeout(15000); // 15s default
  const correlationId = uuid()
  const headers = {
    'Content-Type': 'application/json',
    'X-Request-Id': correlationId,
    ...(options.headers || {}),
  };
  const token = getAuthToken();
  if (token) headers['Authorization'] = `Bearer ${token}`;

  try {
      const result = await fetch(url, { ...options, headers, signal });
      if (!result.ok) {
        const response = await result.text().catch(() => result.statusText);
        const responseObject = { ...JSON.parse(response), status: result.status, correlationId: correlationId };
        const error = JSON.stringify(responseObject);
        console.log(`API Error: ${error}`);
        throw new Error(error);
      }
      const text = await result.text();
      return text ? JSON.parse(text) : null;
  } catch (err) {
    if (err.name === 'AbortError') {
      throw new Error('Request timed out');
    }
    throw err;
  } finally {
    clearTimer();
  }
}

// Convenience methods
export const apiGet = (p) => api(p);
export const apiPost = (p, data) => api(p, { method: 'POST', body: JSON.stringify(data) });
export const apiPut = (p, data) => api(p, { method: 'PUT',  body: JSON.stringify(data) });
export const apiDelete = (p) => api(p, { method: 'DELETE' });

export const parseApiError = (e) => {
    try {
        return JSON.parse(e.message);
    } catch (parseFail) {
        return { response: { code: '???', message: e.message, details: {} }, status: '???', correlationId: '???' };
    }
}

export const errorMessage = (e) => {
    const error = parseApiError(e);
    return error.status === 500
        ? `${error.response.message} (Error ${error.response.code} Status ${error.status} ID ${error.correlationId})`
        : `${error.response.message}`;
}
