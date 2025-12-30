import axios, { AxiosInstance } from 'axios';
import keycloak from './keycloak';

class ApiClient {
  private client: AxiosInstance;

  constructor(baseURL: string) {
    this.client = axios.create({
      baseURL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.client.interceptors.request.use(async (config) => {
      // Попытка получить токен из Keycloak
      if (typeof window !== 'undefined' && keycloak && keycloak.token) {
        // Обновим токен если нужно
        try {
          await keycloak.updateToken(30);
          config.headers.Authorization = `Bearer ${keycloak.token}`;
        } catch (error) {
          console.error('Failed to refresh token', error);
        }
      } else {
        // Fallback на localStorage
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
      }
      return config;
    });
  }

  async get<T>(url: string, params?: any): Promise<T> {
    const response = await this.client.get<T>(url, { params });
    return response.data;
  }

  async post<T>(url: string, data?: any): Promise<T> {
    const response = await this.client.post<T>(url, data);
    return response.data;
  }

  async put<T>(url: string, data?: any): Promise<T> {
    const response = await this.client.put<T>(url, data);
    return response.data;
  }

  async patch<T>(url: string, data?: any): Promise<T> {
    const response = await this.client.patch<T>(url, data);
    return response.data;
  }

  async delete<T>(url: string): Promise<T> {
    const response = await this.client.delete<T>(url);
    return response.data;
  }
}

export const orderServiceApi = new ApiClient(
  process.env.NEXT_PUBLIC_ORDER_SERVICE_URL || 'http://localhost:8080'
);

export const restaurantServiceApi = new ApiClient(
  process.env.NEXT_PUBLIC_RESTAURANT_SERVICE_URL || 'http://localhost:8081'
);

export const userServiceApi = new ApiClient(
  process.env.NEXT_PUBLIC_USER_SERVICE_URL || 'http://localhost:8083'
);

export const paymentServiceApi = new ApiClient(
  process.env.NEXT_PUBLIC_PAYMENT_SERVICE_URL || 'http://localhost:8084'
);

export const notificationServiceApi = new ApiClient(
  process.env.NEXT_PUBLIC_NOTIFICATION_SERVICE_URL || 'http://localhost:8085'
);

