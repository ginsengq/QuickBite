import { create } from 'zustand';
import keycloak from '@/lib/keycloak';

let isInitialized = false;

interface AuthState {
  isAuthenticated: boolean;
  token: string | null;
  user: any | null;
  isLoading: boolean;
  login: () => Promise<void>;
  logout: () => void;
  init: () => Promise<void>;
}

export const useAuthStore = create<AuthState>((set) => ({
  isAuthenticated: false,
  token: null,
  user: null,
  isLoading: true,

  init: async () => {
    // Проверяем что не инициализировали ранее
    if (isInitialized) {
      set({ isLoading: false });
      return;
    }

    // Проверяем что мы на клиенте
    if (typeof window === 'undefined' || !keycloak) {
      set({ isLoading: false });
      return;
    }

    try {
      isInitialized = true;
      const authenticated = await keycloak.init({
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      });

      if (authenticated && keycloak.token) {
        localStorage.setItem('token', keycloak.token);
        set({
          isAuthenticated: true,
          token: keycloak.token,
          user: keycloak.tokenParsed,
          isLoading: false,
        });

        // Refresh token
        setInterval(() => {
          keycloak.updateToken(70).then((refreshed) => {
            if (refreshed && keycloak.token) {
              localStorage.setItem('token', keycloak.token);
              set({ token: keycloak.token });
            }
          });
        }, 60000);
      } else {
        set({ isLoading: false });
      }
    } catch (error) {
      console.error('Failed to initialize Keycloak', error);
      isInitialized = false; // Сброс при ошибке
      set({ isLoading: false });
    }
  },

  login: async () => {
    try {
      await keycloak.login();
    } catch (error) {
      console.error('Login failed', error);
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    keycloak.logout();
    set({ isAuthenticated: false, token: null, user: null });
  },
}));

