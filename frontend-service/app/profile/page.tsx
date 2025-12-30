'use client';

import { useEffect, useState } from 'react';
import { useAuthStore } from '@/store/auth.store';
import { userService } from '@/lib/services/user.service';
import { User } from '@/types';
import { motion } from 'framer-motion';
import { UserCircle, Mail, Phone, MapPin, Edit2, Save } from 'lucide-react';

export default function ProfilePage() {
  const { user: authUser, isAuthenticated } = useAuthStore();
  const [user, setUser] = useState<User | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    phone: '',
    address: '',
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (isAuthenticated && authUser) {
      loadUserProfile();
    }
  }, [isAuthenticated, authUser]);

  const loadUserProfile = async () => {
    try {
      const userData = await userService.getUser(authUser?.sub || 1);
      setUser(userData);
      setFormData({
        firstName: userData.firstName || '',
        lastName: userData.lastName || '',
        phone: userData.phone || '',
        address: userData.address || '',
      });
    } catch (error) {
      console.error('Failed to load user profile', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    if (!user) return;

    try {
      const updatedUser = await userService.updateUser(user.id, formData);
      setUser(updatedUser);
      setIsEditing(false);
      alert('Профиль успешно обновлен!');
    } catch (error) {
      console.error('Failed to update profile', error);
      alert('Ошибка при обновлении профиля');
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 py-12">
        <div className="max-w-4xl mx-auto px-4">
          <div className="animate-pulse">
            <div className="h-64 bg-gray-300 rounded-lg"></div>
          </div>
        </div>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="min-h-screen bg-gray-50 py-12">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <h1 className="text-2xl font-bold text-gray-700">Профиль не найден</h1>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-4xl mx-auto px-4">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <div className="card">
            <div className="flex items-center justify-between mb-6">
              <h1 className="text-3xl font-bold">Мой профиль</h1>
              {!isEditing ? (
                <button
                  onClick={() => setIsEditing(true)}
                  className="btn-primary flex items-center gap-2"
                >
                  <Edit2 size={18} />
                  Редактировать
                </button>
              ) : (
                <div className="flex gap-2">
                  <button
                    onClick={handleSave}
                    className="btn-primary flex items-center gap-2"
                  >
                    <Save size={18} />
                    Сохранить
                  </button>
                  <button
                    onClick={() => {
                      setIsEditing(false);
                      setFormData({
                        firstName: user.firstName || '',
                        lastName: user.lastName || '',
                        phone: user.phone || '',
                        address: user.address || '',
                      });
                    }}
                    className="btn-secondary"
                  >
                    Отмена
                  </button>
                </div>
              )}
            </div>

            <div className="space-y-6">
              {/* Avatar */}
              <div className="flex justify-center mb-6">
                <div className="bg-primary-100 rounded-full p-8">
                  <UserCircle size={80} className="text-primary-600" />
                </div>
              </div>

              {/* User Info */}
              <div className="grid md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Имя
                  </label>
                  {isEditing ? (
                    <input
                      type="text"
                      value={formData.firstName}
                      onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                      className="input"
                    />
                  ) : (
                    <p className="text-gray-900 py-2">{user.firstName || 'Не указано'}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Фамилия
                  </label>
                  {isEditing ? (
                    <input
                      type="text"
                      value={formData.lastName}
                      onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                      className="input"
                    />
                  ) : (
                    <p className="text-gray-900 py-2">{user.lastName || 'Не указано'}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
                    <Mail size={18} />
                    Email
                  </label>
                  <p className="text-gray-900 py-2">{user.email}</p>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
                    <Phone size={18} />
                    Телефон
                  </label>
                  {isEditing ? (
                    <input
                      type="tel"
                      value={formData.phone}
                      onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                      className="input"
                    />
                  ) : (
                    <p className="text-gray-900 py-2">{user.phone || 'Не указано'}</p>
                  )}
                </div>

                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
                    <MapPin size={18} />
                    Адрес доставки
                  </label>
                  {isEditing ? (
                    <textarea
                      value={formData.address}
                      onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                      className="input"
                      rows={3}
                    />
                  ) : (
                    <p className="text-gray-900 py-2">{user.address || 'Не указано'}</p>
                  )}
                </div>
              </div>

              {/* Account Info */}
              <div className="border-t pt-6 mt-6">
                <h2 className="text-xl font-bold mb-4">Информация об аккаунте</h2>
                <div className="grid md:grid-cols-2 gap-4">
                  <div>
                    <p className="text-sm text-gray-600">Логин</p>
                    <p className="font-medium">{user.username}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">Дата регистрации</p>
                    <p className="font-medium">
                      {new Date(user.createdAt).toLocaleDateString('ru-RU')}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </motion.div>
      </div>
    </div>
  );
}

