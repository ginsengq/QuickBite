'use client';

import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { orderService } from '@/lib/services/order.service';
import { paymentService } from '@/lib/services/payment.service';
import { Order, Payment } from '@/types';
import { motion } from 'framer-motion';
import { ArrowLeft, Package, CreditCard } from 'lucide-react';
import Link from 'next/link';

export default function OrderDetailsPage() {
  const params = useParams();
  const router = useRouter();
  const orderId = Number(params.id);

  const [order, setOrder] = useState<Order | null>(null);
  const [payment, setPayment] = useState<Payment | null>(null);
  const [loading, setLoading] = useState(true);
  const [showPaymentForm, setShowPaymentForm] = useState(false);

  useEffect(() => {
    if (orderId) {
      loadOrderDetails();
    }
  }, [orderId]);

  const loadOrderDetails = async () => {
    try {
      const orderData = await orderService.getOrder(orderId);
      setOrder(orderData);

      try {
        const paymentData = await paymentService.getPaymentByOrder(orderId);
        setPayment(paymentData);
      } catch (error) {
        console.log('No payment found for this order');
      }
    } catch (error) {
      console.error('Failed to load order details', error);
    } finally {
      setLoading(false);
    }
  };

  const handlePayment = async (paymentMethod: 'CARD' | 'CASH' | 'WALLET') => {
    try {
      const paymentData = await paymentService.createPayment({
        orderId: orderId,
        paymentMethod: paymentMethod,
      });
      setPayment(paymentData);
      setShowPaymentForm(false);
      
      // Перезагрузить заказ чтобы обновить статус
      await loadOrderDetails();
      
      alert('Оплата успешно обработана!');
    } catch (error) {
      console.error('Payment failed', error);
      alert('Ошибка при обработке платежа');
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 py-12">
        <div className="max-w-4xl mx-auto px-4">
          <div className="animate-pulse space-y-4">
            <div className="h-64 bg-gray-300 rounded-lg"></div>
          </div>
        </div>
      </div>
    );
  }

  if (!order) {
    return (
      <div className="min-h-screen bg-gray-50 py-12">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <h1 className="text-2xl font-bold text-gray-700">Заказ не найден</h1>
          <Link href="/orders" className="text-primary-600 hover:text-primary-700 mt-4 inline-block">
            Вернуться к заказам
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-4xl mx-auto px-4">
        <Link href="/orders" className="inline-flex items-center gap-2 text-gray-600 hover:text-primary-600 mb-6">
          <ArrowLeft size={20} />
          Назад к заказам
        </Link>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <div className="card">
            <div className="flex items-center justify-between mb-6">
              <div>
                <h1 className="text-3xl font-bold mb-2">Заказ #{order.id}</h1>
                <p className="text-gray-600">
                  {new Date(order.createdAt).toLocaleString('ru-RU')}
                </p>
              </div>
              <div className="text-right">
                <p className="text-sm text-gray-600 mb-1">Статус</p>
                <p className="text-lg font-semibold text-primary-600">
                  {order.status}
                </p>
              </div>
            </div>

            {/* Order Items */}
            <div className="border-t pt-6 mb-6">
              <h2 className="text-xl font-bold mb-4">Состав заказа</h2>
              <div className="space-y-3">
                {order.items?.map((item) => (
                  <div key={item.id} className="flex justify-between items-center">
                    <div className="flex-1">
                      <p className="font-medium">{item.menuItem?.name || `Товар #${item.menuItemId}`}</p>
                      <p className="text-sm text-gray-600">Количество: {item.quantity}</p>
                    </div>
                    <p className="font-semibold">
                      {((item.price || 0) * item.quantity).toFixed(2)} ₸
                    </p>
                  </div>
                ))}
              </div>
            </div>

            {/* Total */}
            <div className="border-t pt-6 mb-6">
              <div className="flex justify-between text-2xl font-bold">
                <span>Итого:</span>
                <span className="text-primary-600">{order.totalPrice?.toFixed(2) || '0.00'} ₸</span>
              </div>
            </div>

            {/* Payment Section */}
            <div className="border-t pt-6">
              <h2 className="text-xl font-bold mb-4 flex items-center gap-2">
                <CreditCard size={24} />
                Оплата
              </h2>

              {payment ? (
                <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                  <p className="text-green-800 font-semibold mb-2">
                    Оплата {payment.status === 'COMPLETED' ? 'завершена' : payment.status}
                  </p>
                  <p className="text-sm text-gray-600">
                    Метод: {payment.paymentMethod}
                  </p>
                  <p className="text-sm text-gray-600">
                    Сумма: {payment.amount.toFixed(2)} ₸
                  </p>
                </div>
              ) : (
                <div>
                  {!showPaymentForm ? (
                    <button
                      onClick={() => setShowPaymentForm(true)}
                      className="btn-primary"
                    >
                      Оплатить заказ
                    </button>
                  ) : (
                    <div className="space-y-3">
                      <p className="text-gray-600 mb-4">Выберите способ оплаты:</p>
                      <button
                        onClick={() => handlePayment('CARD')}
                        className="btn-primary w-full"
                      >
                        Банковская карта
                      </button>
                      <button
                        onClick={() => handlePayment('CASH')}
                        className="btn-secondary w-full"
                      >
                        Наличные при получении
                      </button>
                      <button
                        onClick={() => handlePayment('WALLET')}
                        className="btn-secondary w-full"
                      >
                        Электронный кошелек
                      </button>
                      <button
                        onClick={() => setShowPaymentForm(false)}
                        className="text-gray-600 hover:text-gray-800 w-full text-center py-2"
                      >
                        Отмена
                      </button>
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </motion.div>
      </div>
    </div>
  );
}

