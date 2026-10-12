/**
 * InteriorAI - Authentication and Session Management
 */

const Auth = {
  TOKEN_KEY: 'interiorai_token',
  USER_KEY: 'interiorai_user',

  getToken() {
    return localStorage.getItem(this.TOKEN_KEY);
  },

  getUser() {
    try {
      const data = localStorage.getItem(this.USER_KEY);
      return data ? JSON.parse(data) : null;
    } catch {
      return null;
    }
  },

  isLoggedIn() {
    return !!this.getToken();
  },

  setSession(token, user) {
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  },

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    window.location.href = 'index.html';
  },

  requireAuth() {
    if (!this.isLoggedIn()) {
      const redirect = encodeURIComponent(window.location.pathname + window.location.search);
      window.location.href = `login.html?redirect=${redirect}`;
      return false;
    }
    return true;
  },

  initNav() {
    const user = this.getUser();
    const navActions = document.getElementById('nav-actions');
    const myProjectsLink = document.getElementById('nav-my-projects');
    const bookingsLink = document.getElementById('nav-bookings');

    if (myProjectsLink) {
      myProjectsLink.style.display = user ? 'block' : 'none';
    }
    if (bookingsLink) {
      bookingsLink.style.display = user ? 'block' : 'none';
    }

    if (navActions) {
      if (user) {
        navActions.innerHTML = `
          <div style="display: flex; align-items: center; gap: 12px;">
            <a href="dashboard.html" class="btn btn-secondary btn-sm" style="display: flex; align-items: center; gap: 6px;">
              <span style="display: inline-block; width: 8px; height: 8px; border-radius: 50%; background: var(--accent-sage);"></span>
              Dashboard
            </a>
            <div style="position: relative; display: inline-block;" class="user-dropdown">
              <a href="profile.html" class="btn btn-outline btn-sm" style="display: flex; align-items: center; gap: 6px;">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
                <span>${user.fullName ? user.fullName.split(' ')[0] : 'Account'}</span>
              </a>
            </div>
            <button onclick="Auth.logout()" class="btn btn-sm" style="background: transparent; color: var(--text-muted); border: none; padding: 6px 10px; cursor: pointer;" title="Logout">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
            </button>
          </div>
        `;
      } else {
        navActions.innerHTML = `
          <a href="login.html" class="btn btn-secondary btn-sm">Log In</a>
          <a href="register.html" class="btn btn-primary btn-sm">Get Started</a>
        `;
      }
    }
  }
};

document.addEventListener('DOMContentLoaded', () => {
  Auth.initNav();
});
