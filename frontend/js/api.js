/**
 * InteriorAI - Unified API Client
 * Automatically manages Authorization headers, base URLs, and errors.
 */

const API = {
  // Configured default production backend URL
  // Can be overridden via:
  // 1. window.INTERIORAI_API_URL
  // 2. <meta name="interiorai-api-url" content="..."> in HTML head
  // 3. localStorage.getItem('interiorai_api_url')
  PROD_BACKEND_URL: 'https://interiorai-backend.vercel.app',

  getBaseUrl() {
    // 1. Explicit global window override (set in HTML, config.js, or Vercel)
    if (window.INTERIORAI_API_URL && window.INTERIORAI_API_URL.trim() !== '') {
      const clean = window.INTERIORAI_API_URL.trim().replace(/\/$/, '');
      return clean.endsWith('/api') ? clean : clean + '/api';
    }

    // 2. Meta tag override (<meta name="interiorai-api-url" content="https://...">)
    const metaTag = document.querySelector('meta[name="interiorai-api-url"]');
    if (metaTag && metaTag.content && !metaTag.content.startsWith('__') && metaTag.content.trim() !== '') {
      const clean = metaTag.content.trim().replace(/\/$/, '');
      return clean.endsWith('/api') ? clean : clean + '/api';
    }

    // 3. LocalStorage override (for testing in browser DevTools: localStorage.setItem('interiorai_api_url', '...'))
    const storedUrl = localStorage.getItem('interiorai_api_url');
    if (storedUrl && storedUrl.trim() !== '') {
      const clean = storedUrl.trim().replace(/\/$/, '');
      return clean.endsWith('/api') ? clean : clean + '/api';
    }

    // 4. Localhost development detection
    const isLocalhost = window.location.hostname === 'localhost' ||
                        window.location.hostname === '127.0.0.1' ||
                        window.location.protocol === 'file:' ||
                        !window.location.hostname;

    if (isLocalhost) {
      // If frontend is served directly by Spring Boot on port 8080:
      if (window.location.port === '8080') {
        return '/api';
      }
      // Local standalone frontend dev server (e.g. port 3000, 5500, or file://)
      return 'http://localhost:8080/api';
    }

    // 5. Cloud / Vercel deployed frontend:
    // If PROD_BACKEND_URL is set, use it; otherwise use relative /api (proxied by vercel.json)
    if (this.PROD_BACKEND_URL && this.PROD_BACKEND_URL.trim() !== '') {
      const clean = this.PROD_BACKEND_URL.trim().replace(/\/$/, '');
      return clean.endsWith('/api') ? clean : clean + '/api';
    }

    return '/api';
  },

  getAssetUrl(path) {
    if (!path) return '';
    if (path.startsWith('http://') || path.startsWith('https://') || path.startsWith('data:')) {
      return path;
    }

    const cleanPath = path.startsWith('/') ? path : '/' + path;
    const isLocalhost = window.location.hostname === 'localhost' ||
                        window.location.hostname === '127.0.0.1' ||
                        window.location.protocol === 'file:' ||
                        !window.location.hostname;

    if (isLocalhost) {
      return window.location.port === '8080' ? cleanPath : 'http://localhost:8080' + cleanPath;
    }

    // Production / Vercel: route to backend origin
    const baseApi = this.getBaseUrl();
    const backendOrigin = baseApi.replace(/\/api\/?$/, '');
    return backendOrigin ? backendOrigin + cleanPath : cleanPath;
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
