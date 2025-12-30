import { notificationServiceApi } from '../api-client';
import { Notification } from '@/types';

export const notificationService = {
  async getUserNotifications(userId: number): Promise<Notification[]> {
    return notificationServiceApi.get<Notification[]>(`/api/notifications/user/${userId}`);
  },

  async getAllNotifications(): Promise<Notification[]> {
    return notificationServiceApi.get<Notification[]>('/api/notifications');
  },
};

