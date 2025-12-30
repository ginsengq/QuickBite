// Order Service Types
export interface Order {
  id: number;
  userId: number;
  restaurantId: number;
  status: 'CREATED' | 'PENDING' | 'CONFIRMED' | 'PREPARING' | 'READY' | 'DELIVERING' | 'DELIVERED' | 'CANCELLED';
  totalPrice: number;
  createdAt: string;
  updatedAt: string;
  items: OrderItem[];
}

export interface OrderItem {
  id?: number;
  menuItemId: number;
  quantity: number;
  price?: number;
  menuItem?: MenuItem;
}

export interface CreateOrderRequest {
  userId: number;
  restaurantId: number;
  items: {
    menuItemId: number;
    quantity: number;
  }[];
}

// Restaurant Service Types
export interface Restaurant {
  id: number;
  name: string;
  address: string;
  phone: string;
  rating?: number;
  imageUrl?: string;
  description?: string;
}

export interface MenuItem {
  id: number;
  restaurantId: number;
  name: string;
  description: string;
  price: number;
  imageUrl?: string;
  available: boolean;
  categories?: Category[];
}

export interface Category {
  id: number;
  name: string;
  description?: string;
}

// User Service Types
export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  address?: string;
  createdAt: string;
}

export interface CreateUserRequest {
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  address?: string;
}

// Payment Service Types
export interface Payment {
  id: number;
  orderId: number;
  userId: number;
  amount: number;
  status: 'PENDING' | 'COMPLETED' | 'FAILED' | 'REFUNDED';
  paymentMethod: 'CARD' | 'CASH' | 'WALLET';
  createdAt: string;
  updatedAt: string;
}

export interface CreatePaymentRequest {
  orderId: number;
  paymentMethod: 'CARD' | 'CASH' | 'WALLET';
  cardNumber?: string;
  cardHolderName?: string;
  expiryDate?: string;
  cvv?: string;
}

// Notification Service Types
export interface Notification {
  id: number;
  userId: number;
  type: 'ORDER_CREATED' | 'PAYMENT_COMPLETED' | 'WELCOME' | 'ORDER_STATUS_CHANGED';
  message: string;
  read: boolean;
  createdAt: string;
}

