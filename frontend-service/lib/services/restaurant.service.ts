import { restaurantServiceApi } from '../api-client';
import { Restaurant, MenuItem, Category } from '@/types';

export const restaurantService = {
  async getRestaurants(): Promise<Restaurant[]> {
    return restaurantServiceApi.get<Restaurant[]>('/api/restaurants');
  },

  async getRestaurant(id: number): Promise<Restaurant> {
    return restaurantServiceApi.get<Restaurant>(`/api/restaurants/${id}`);
  },

  async searchRestaurants(keyword: string): Promise<Restaurant[]> {
    return restaurantServiceApi.get<Restaurant[]>(`/api/restaurants/search?keyword=${keyword}`);
  },

  async getCategories(): Promise<Category[]> {
    return restaurantServiceApi.get<Category[]>('/api/categories');
  },

  async getMenuItemsByRestaurant(restaurantId: number): Promise<MenuItem[]> {
    return restaurantServiceApi.get<MenuItem[]>(`/api/menu-items/restaurant/${restaurantId}`);
  },

  async getMenuItemsByCategory(categoryId: number): Promise<MenuItem[]> {
    return restaurantServiceApi.get<MenuItem[]>(`/api/menu-items/category/${categoryId}`);
  },

  async getMenuItemPrices(ids: number[]): Promise<{ [key: number]: number }> {
    return restaurantServiceApi.get<{ [key: number]: number }>(`/api/menu-items/prices?ids=${ids.join(',')}`);
  },
};

