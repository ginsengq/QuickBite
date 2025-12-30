'use client';

import { useCartStore } from '@/store/cart.store';
import { ShoppingCart } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { motion, AnimatePresence } from 'framer-motion';

export default function FloatingCart() {
  const router = useRouter();
  const items = useCartStore((state) => state.items);
  
  const itemCount = items.reduce((sum, item) => sum + item.quantity, 0);
  const total = items.reduce((sum, item) => sum + item.menuItem.price * item.quantity, 0);

  if (itemCount === 0) return null;

  return (
    <AnimatePresence>
      <motion.button
        initial={{ scale: 0, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        exit={{ scale: 0, opacity: 0 }}
        whileHover={{ scale: 1.1 }}
        whileTap={{ scale: 0.95 }}
        onClick={() => router.push('/cart')}
        className="fixed bottom-6 right-6 bg-orange-500 hover:bg-orange-600 text-white rounded-full p-4 shadow-lg flex items-center gap-3 z-50 transition-colors"
      >
        <div className="relative">
          <ShoppingCart className="w-6 h-6" />
          <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center font-bold">
            {itemCount}
          </span>
        </div>
        <div className="flex flex-col items-start">
          <span className="text-xs opacity-90">Корзина</span>
          <span className="font-bold">{total.toFixed(2)} ₸</span>
        </div>
      </motion.button>
    </AnimatePresence>
  );
}
