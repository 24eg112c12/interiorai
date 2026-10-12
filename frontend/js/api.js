/**
 * InteriorAI - Unified API Client
 * Automatically manages Authorization headers, base URLs, and errors.
 */

const API = {
  getBaseUrl() {
    // 1. Explicit override if set
    if (window.INTERIORAI_API_URL) {
      return window.INTERIORAI_API_URL;
    }
    // 2. Cloud or Vercel deployment (non-localhost)
    if (window.location.hostname && window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1' && window.location.protocol.startsWith('http')) {
      return '/api';
    }
    // 3. Served directly by Spring Boot on port 8080
    if (window.location.port === '8080') {
      return '/api';
    }
    // 4. Local frontend dev server (e.g. port 3000, 5500, or file://)
    return 'http://localhost:8080/api';
  },

  getAssetUrl(path) {
    if (!path) return '';
    if (path.startsWith('http://') || path.startsWith('https://') || path.startsWith('data:')) {
      return path;
    }
    const isCloud = window.location.hostname && window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1' && window.location.protocol.startsWith('http');
    const base = isCloud || window.location.port === '8080' ? '' : 'http://localhost:8080';
    return base + (path.startsWith('/') ? path : '/' + path);
  },

  async request(endpoint, options = {}) {
    const url = `${this.getBaseUrl()}${endpoint.startsWith('/') ? endpoint : '/' + endpoint}`;
    const token = localStorage.getItem('interiorai_token');

    const headers = {
      ...(options.headers || {})
    };

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    // Don't set Content-Type if sending FormData (browser automatically sets multipart boundary)
    if (!(options.body instanceof FormData) && !headers['Content-Type']) {
      headers['Content-Type'] = 'application/json';
    }

    try {
      const response = await fetch(url, {
        ...options,
        headers
      });

      // Handle 401 Unauthorized
      if (response.status === 401) {
        if (!endpoint.includes('/auth/login') && !endpoint.includes('/auth/register')) {
          localStorage.removeItem('interiorai_token');
          localStorage.removeItem('interiorai_user');
          if (window.location.pathname.includes('dashboard') ||
              window.location.pathname.includes('design') ||
              window.location.pathname.includes('project') ||
              window.location.pathname.includes('booking')) {
            window.location.href = 'login.html?session=expired';
          }
        }
      }

      const data = await response.json().catch(() => null);

      if (!response.ok) {
        const errorMsg = data?.message || (data?.data && typeof data.data === 'object' ? Object.values(data.data).join(', ') : 'Request failed');
        throw new Error(errorMsg);
      }

      return data;
    } catch (err) {
      console.error(`API Error [${endpoint}]:`, err);
      throw err;
    }
  },

  get(endpoint) {
    return this.request(endpoint, { method: 'GET' });
  },

  post(endpoint, body) {
    const isFormData = body instanceof FormData;
    return this.request(endpoint, {
      method: 'POST',
      body: isFormData ? body : JSON.stringify(body)
    });
  },

  put(endpoint, body) {
    return this.request(endpoint, {
      method: 'PUT',
      body: JSON.stringify(body)
    });
  },

  patch(endpoint, body) {
    return this.request(endpoint, {
      method: 'PATCH',
      body: JSON.stringify(body)
    });
  },

  delete(endpoint) {
    return this.request(endpoint, { method: 'DELETE' });
  }
};
