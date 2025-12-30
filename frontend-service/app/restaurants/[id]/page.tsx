'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { restaurantService } from '@/lib/services/restaurant.service';
import { Restaurant, MenuItem } from '@/types';
import MenuItemCard from '@/components/MenuItemCard';
import FloatingCart from '@/components/FloatingCart';
import { motion } from 'framer-motion';
import { MapPin, Phone, Star, ArrowLeft } from 'lucide-react';
import Link from 'next/link';

export default function RestaurantDetailsPage() {
  const params = useParams();
  const restaurantId = Number(params.id);

  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (restaurantId) {
      loadRestaurantDetails();
    }
  }, [restaurantId]);

  const loadRestaurantDetails = async () => {
    try {
      const [restaurantData, menuData] = await Promise.all([
        restaurantService.getRestaurant(restaurantId),
        restaurantService.getMenuItemsByRestaurant(restaurantId),
      ]);
      setRestaurant(restaurantData);
      setMenuItems(menuData);
    } catch (error) {
      console.error('Failed to load restaurant details', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 py-12">
        <div className="max-w-7xl mx-auto px-4">
          <div className="animate-pulse space-y-8">
            <div className="h-64 bg-gray-300 rounded-lg"></div>
            <div className="h-8 bg-gray-300 rounded w-1/3"></div>
            <div className="grid md:grid-cols-3 gap-6">
              {[1, 2, 3, 4, 5, 6].map((i) => (
                <div key={i} className="h-48 bg-gray-300 rounded-lg"></div>
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (!restaurant) {
    return (
      <div className="min-h-screen bg-gray-50 py-12">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <h1 className="text-2xl font-bold text-gray-700">Ресторан не найден</h1>
          <Link href="/restaurants" className="text-primary-600 hover:text-primary-700 mt-4 inline-block">
            Вернуться к списку ресторанов
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Restaurant Header */}
      <div className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 py-8">
          <Link href="/restaurants" className="inline-flex items-center gap-2 text-gray-600 hover:text-primary-600 mb-4">
            <ArrowLeft size={20} />
            Назад к ресторанам
          </Link>

          <div className="flex flex-col md:flex-row gap-8">
            {restaurant.imageUrl && (
              <img
                src={restaurant.imageUrl}
                alt={restaurant.name}
                className="w-full md:w-64 h-64 object-cover rounded-lg"
              />
            )}

            <div className="flex-1">
              <motion.h1
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                className="text-4xl font-bold mb-4"
              >
                {restaurant.name}
              </motion.h1>

              {restaurant.rating && (
                <div className="flex items-center gap-2 mb-4">
                  <Star className="text-yellow-400 fill-current" size={24} />
                  <span className="text-xl font-medium">{restaurant.rating.toFixed(1)}</span>
                </div>
              )}

              <div className="space-y-3 text-gray-600">
                <div className="flex items-start gap-3">
                  <MapPin size={20} className="mt-0.5 flex-shrink-0" />
                  <span>{restaurant.address}</span>
                </div>

                <div className="flex items-center gap-3">
                  <Phone size={20} className="flex-shrink-0" />
                  <span>{restaurant.phone}</span>
                </div>
              </div>

              {restaurant.description && (
                <p className="text-gray-700 mt-4">{restaurant.description}</p>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Menu Items */}
      <div className="max-w-7xl mx-auto px-4 py-12">
        <motion.h2
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-3xl font-bold mb-8"
        >
          Меню
        </motion.h2>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {menuItems.map((item, index) => (
            <motion.div
              key={item.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.05 }}
            >
              <MenuItemCard menuItem={item} />
            </motion.div>
          ))}
        </div>

        {menuItems.length === 0 && (
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">Меню пока не добавлено</p>
          </div>
        )}
      </div>

      <FloatingCart />
    </div>
  );
}

