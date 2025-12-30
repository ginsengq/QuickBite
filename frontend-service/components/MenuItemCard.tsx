'use client';

import { MenuItem } from '@/types';
import { useCartStore } from '@/store/cart.store';
import { Plus, Minus } from 'lucide-react';
import { useState } from 'react';
import { getMenuItemImage } from '@/lib/placeholder';

interface MenuItemCardProps {
  menuItem: MenuItem;
}

export default function MenuItemCard({ menuItem }: MenuItemCardProps) {
  const { addItem, items, updateQuantity } = useCartStore();
  const cartItem = items.find(item => item.menuItem.id === menuItem.id);
  const [quantity, setQuantity] = useState(cartItem?.quantity || 0);

  const handleAddToCart = () => {
    addItem(menuItem, 1);
    setQuantity(quantity + 1);
  };

  const handleIncrement = () => {
    updateQuantity(menuItem.id, quantity + 1);
    setQuantity(quantity + 1);
  };

  const handleDecrement = () => {
    if (quantity > 0) {
      updateQuantity(menuItem.id, quantity - 1);
      setQuantity(quantity - 1);
    }
  };

  return (
    <div className="card">
      <img
        src={getMenuItemImage(menuItem.imageUrl, menuItem.name)}
        alt={menuItem.name}
        className="w-full h-48 object-cover rounded-lg -mt-6 -mx-6 mb-4"
        onError={(e) => {
          // Fallback если изображение не загрузилось
          const id = Math.abs(menuItem.id.hashCode()) % 100 + 1;
          e.currentTarget.src = `https://picsum.photos/400/300?random=${id}`;
        }}
      />

      <h3 className="text-xl font-semibold mb-2">{menuItem.name}</h3>

      {menuItem.description && (
        <p className="text-gray-600 text-sm mb-4 line-clamp-2">
          {menuItem.description}
        </p>
      )}

      <div className="flex items-center justify-between mt-auto">
        <span className="text-2xl font-bold text-primary-600">
          {menuItem.price.toFixed(2)} ₸
        </span>

        {!menuItem.available ? (
          <span className="text-gray-500 text-sm">Недоступно</span>
        ) : quantity > 0 ? (
          <div className="flex items-center gap-2">
            <button
              onClick={handleDecrement}
              className="bg-gray-200 hover:bg-gray-300 p-2 rounded-lg transition-colors"
            >
              <Minus size={18} />
            </button>
            <span className="font-semibold w-8 text-center">{quantity}</span>
            <button
              onClick={handleIncrement}
              className="bg-primary-600 hover:bg-primary-700 text-white p-2 rounded-lg transition-colors"
            >
              <Plus size={18} />
            </button>
          </div>
        ) : (
          <button
            onClick={handleAddToCart}
            className="btn-primary"
          >
            Добавить
          </button>
        )}
      </div>
    </div>
  );
}

