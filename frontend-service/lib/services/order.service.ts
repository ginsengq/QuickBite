import { orderServiceApi } from '../api-client';
import { Order, CreateOrderRequest } from '@/types';

export const orderService = {
  async createOrder(data: CreateOrderRequest): Promise<Order> {
    return orderServiceApi.post<Order>('/api/orders', data);
  },

  async getOrder(id: number): Promise<Order> {
    return orderServiceApi.get<Order>(`/api/orders/${id}`);
  },

  async getUserOrders(userId: number): Promise<Order[]> {
    return orderServiceApi.get<Order[]>(`/api/orders?userId=${userId}`);
  },

  async updateOrderStatus(id: number, status: string): Promise<Order> {
    return orderServiceApi.patch<Order>(`/api/orders/${id}/status`, { status });
  },
};

