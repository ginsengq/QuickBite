import { create } from 'zustand';
import { MenuItem } from '@/types';

interface CartItem {
  menuItem: MenuItem;
  quantity: number;
}

interface CartState {
  items: CartItem[];
  restaurantId: number | null;
  addItem: (menuItem: MenuItem, quantity?: number) => void;
  removeItem: (menuItemId: number) => void;
  updateQuantity: (menuItemId: number, quantity: number) => void;
  clearCart: () => void;
  getTotalAmount: () => number;
  getItemCount: () => number;
}

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  restaurantId: null,

  addItem: (menuItem: MenuItem, quantity = 1) => {
    const { items, restaurantId } = get();

    // Проверяем, что все товары из одного ресторана
    if (restaurantId && restaurantId !== menuItem.restaurantId) {
      if (!confirm('Вы можете заказать только из одного ресторана. Очистить корзину?')) {
        return;
      }
      set({ items: [], restaurantId: null });
    }

    const existingItem = items.find(item => item.menuItem.id === menuItem.id);

    if (existingItem) {
      set({
        items: items.map(item =>
          item.menuItem.id === menuItem.id
            ? { ...item, quantity: item.quantity + quantity }
            : item
        ),
      });
    } else {
      set({
        items: [...items, { menuItem, quantity }],
        restaurantId: menuItem.restaurantId,
      });
    }
  },

  removeItem: (menuItemId: number) => {
    const { items } = get();
    const newItems = items.filter(item => item.menuItem.id !== menuItemId);
    set({
      items: newItems,
      restaurantId: newItems.length > 0 ? get().restaurantId : null,
    });
  },

  updateQuantity: (menuItemId: number, quantity: number) => {
    if (quantity <= 0) {
      get().removeItem(menuItemId);
      return;
    }
    set({
      items: get().items.map(item =>
        item.menuItem.id === menuItemId
          ? { ...item, quantity }
          : item
      ),
    });
  },

  clearCart: () => {
    set({ items: [], restaurantId: null });
  },

  getTotalAmount: () => {
    return get().items.reduce(
      (total, item) => total + item.menuItem.price * item.quantity,
      0
    );
  },

  getItemCount: () => {
    return get().items.reduce((count, item) => count + item.quantity, 0);
  },
}));

