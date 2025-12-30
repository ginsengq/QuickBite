'use client';

import { useEffect, useState } from 'react';
import { useAuthStore } from '@/store/auth.store';
import { notificationService } from '@/lib/services/notification.service';
import { Notification } from '@/types';
import { motion } from 'framer-motion';
import { Bell, Package, CreditCard, UserPlus, CheckCircle } from 'lucide-react';

export default function NotificationsPage() {
  const { user, isAuthenticated } = useAuthStore();
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (isAuthenticated && user) {
      loadNotifications();
    }
  }, [isAuthenticated, user]);

  const loadNotifications = async () => {
    try {
      const data = await notificationService.getUserNotifications(user?.sub || 1);
      setNotifications(data);
    } catch (error) {
      console.error('Failed to load notifications', error);
    } finally {
      setLoading(false);
    }
  };

  const getNotificationIcon = (type: string) => {
    switch (type) {
      case 'ORDER_CREATED':
        return <Package className="text-blue-500" size={24} />;
      case 'PAYMENT_COMPLETED':
        return <CreditCard className="text-green-500" size={24} />;
      case 'WELCOME':
        return <UserPlus className="text-purple-500" size={24} />;
      case 'ORDER_STATUS_CHANGED':
        return <CheckCircle className="text-primary-600" size={24} />;
      default:
        return <Bell className="text-gray-500" size={24} />;
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 py-12">
        <div className="max-w-4xl mx-auto px-4">
          <div className="animate-pulse space-y-4">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-24 bg-gray-300 rounded-lg"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-4xl mx-auto px-4">
        <motion.h1
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-4xl font-bold mb-8"
        >
          Уведомления
        </motion.h1>

        {notifications.length === 0 ? (
          <div className="text-center py-12">
            <Bell size={80} className="mx-auto text-gray-300 mb-4" />
            <h2 className="text-2xl font-bold text-gray-700 mb-2">
              Нет уведомлений
            </h2>
            <p className="text-gray-600">
              Здесь будут появляться уведомления о ваших заказах
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {notifications.map((notification, index) => (
              <motion.div
                key={notification.id}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: index * 0.05 }}
                className={`card ${!notification.read ? 'border-l-4 border-primary-600' : ''}`}
              >
                <div className="flex gap-4">
                  <div className="flex-shrink-0">
                    {getNotificationIcon(notification.type)}
                  </div>
                  <div className="flex-1">
                    <p className="text-gray-900 mb-1">{notification.message}</p>
                    <p className="text-sm text-gray-500">
                      {new Date(notification.createdAt).toLocaleString('ru-RU')}
                    </p>
                  </div>
                  {!notification.read && (
                    <div className="flex-shrink-0">
                      <span className="inline-block w-2 h-2 bg-primary-600 rounded-full"></span>
                    </div>
                  )}
                </div>
              </motion.div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

