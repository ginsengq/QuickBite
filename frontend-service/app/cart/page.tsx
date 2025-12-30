'use client';

import { useCartStore } from '@/store/cart.store';
import { useAuthStore } from '@/store/auth.store';
import { useRouter } from 'next/navigation';
import { motion } from 'framer-motion';
import { Trash2, Plus, Minus, ShoppingBag } from 'lucide-react';
import Link from 'next/link';
import { useState } from 'react';
import { orderService } from '@/lib/services/order.service';

export default function CartPage() {
  const router = useRouter();
  const { items, removeItem, updateQuantity, clearCart, getTotalAmount, restaurantId } = useCartStore();
  const { isAuthenticated, user } = useAuthStore();
  const [isProcessing, setIsProcessing] = useState(false);

  const handleCheckout = async () => {
    if (!isAuthenticated) {
      router.push('/auth/login');
      return;
    }

    if (items.length === 0) return;

    setIsProcessing(true);
    try {
      const orderData = {
        userId: user?.sub || 1,
        restaurantId: restaurantId!,
        items: items.map(item => ({
          menuItemId: item.menuItem.id,
          quantity: item.quantity,
        })),
      };

      const order = await orderService.createOrder(orderData);
      clearCart();
      router.push(`/orders/${order.id}`);
    } catch (error) {
      console.error('Failed to create order', error);
      alert('Не удалось создать заказ. Попробуйте еще раз.');
    } finally {
      setIsProcessing(false);
    }
  };

  if (items.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 py-12">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
          >
            <ShoppingBag size={80} className="mx-auto text-gray-300 mb-4" />
            <h1 className="text-3xl font-bold mb-4">Корзина пуста</h1>
            <p className="text-gray-600 mb-8">Добавьте блюда из меню ресторанов</p>
            <Link href="/restaurants">
              <button className="btn-primary">
                Перейти к ресторанам
              </button>
            </Link>
          </motion.div>
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
          Корзина
        </motion.h1>

        <div className="grid lg:grid-cols-3 gap-8">
          {/* Cart Items */}
          <div className="lg:col-span-2 space-y-4">
            {items.map((item, index) => (
              <motion.div
                key={item.menuItem.id}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: index * 0.1 }}
                className="card flex gap-4"
              >
                {item.menuItem.imageUrl && (
                  <img
                    src={item.menuItem.imageUrl}
                    alt={item.menuItem.name}
                    className="w-24 h-24 object-cover rounded-lg"
                  />
                )}

                <div className="flex-1">
                  <h3 className="text-lg font-semibold mb-1">
                    {item.menuItem.name}
                  </h3>
                  <p className="text-gray-600 text-sm mb-2 line-clamp-1">
                    {item.menuItem.description}
                  </p>
                  <p className="text-primary-600 font-bold">
                    {item.menuItem.price.toFixed(2)} ₽
                  </p>
                </div>

                <div className="flex flex-col justify-between items-end">
                  <button
                    onClick={() => removeItem(item.menuItem.id)}
                    className="text-red-500 hover:text-red-700 transition-colors"
                  >
                    <Trash2 size={20} />
                  </button>

                  <div className="flex items-center gap-2">
                    <button
                      onClick={() => updateQuantity(item.menuItem.id, item.quantity - 1)}
                      className="bg-gray-200 hover:bg-gray-300 p-1 rounded transition-colors"
                    >
                      <Minus size={16} />
                    </button>
                    <span className="font-semibold w-8 text-center">
                      {item.quantity}
                    </span>
                    <button
                      onClick={() => updateQuantity(item.menuItem.id, item.quantity + 1)}
                      className="bg-primary-600 hover:bg-primary-700 text-white p-1 rounded transition-colors"
                    >
                      <Plus size={16} />
                    </button>
                  </div>
                </div>
              </motion.div>
            ))}
          </div>

          {/* Order Summary */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="lg:col-span-1"
          >
            <div className="card sticky top-24">
              <h2 className="text-2xl font-bold mb-4">Итого</h2>

              <div className="space-y-3 mb-6">
                <div className="flex justify-between text-gray-600">
                  <span>Сумма:</span>
                  <span>{getTotalAmount().toFixed(2)} ₽</span>
                </div>
                <div className="flex justify-between text-gray-600">
                  <span>Доставка:</span>
                  <span>Бесплатно</span>
                </div>
                <div className="border-t pt-3 flex justify-between text-xl font-bold">
                  <span>Всего:</span>
                  <span className="text-primary-600">
                    {getTotalAmount().toFixed(2)} ₽
                  </span>
                </div>
              </div>

              <button
                onClick={handleCheckout}
                disabled={isProcessing}
                className="btn-primary w-full disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isProcessing ? 'Обработка...' : 'Оформить заказ'}
              </button>

              <button
                onClick={clearCart}
                className="btn-secondary w-full mt-3"
              >
                Очистить корзину
              </button>
            </div>
          </motion.div>
        </div>
      </div>
    </div>
  );
}

