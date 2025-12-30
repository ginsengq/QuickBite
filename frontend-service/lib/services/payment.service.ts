import { paymentServiceApi } from '../api-client';
import { Payment, CreatePaymentRequest } from '@/types';

export const paymentService = {
  async createPayment(data: CreatePaymentRequest): Promise<Payment> {
    return paymentServiceApi.post<Payment>('/api/payments', data);
  },

  async getPayment(id: number): Promise<Payment> {
    return paymentServiceApi.get<Payment>(`/api/payments/${id}`);
  },

  async getPaymentByOrder(orderId: number): Promise<Payment> {
    return paymentServiceApi.get<Payment>(`/api/payments/order/${orderId}`);
  },

  async getUserPayments(userId: number): Promise<Payment[]> {
    return paymentServiceApi.get<Payment[]>(`/api/payments/user/${userId}`);
  },

  async getAllPayments(): Promise<Payment[]> {
    return paymentServiceApi.get<Payment[]>('/api/payments');
  },
};

