'use client';

import { useAuthStore } from '@/store/auth.store';
import { useCartStore } from '@/store/cart.store';
import Link from 'next/link';
import { ShoppingCart, User, LogOut, Bell } from 'lucide-react';
import { useState, useEffect } from 'react';

export default function Navbar() {
  const { isAuthenticated, user, login, logout, isLoading } = useAuthStore();
  const { items, getTotalAmount, getItemCount } = useCartStore();
  
  const cartCount = getItemCount();
  const cartTotal = getTotalAmount();

  if (isLoading) {
    return (
      <nav className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            <div className="animate-pulse bg-gray-300 h-8 w-32 rounded"></div>
          </div>
        </div>
      </nav>
    );
  }

  return (
    <nav className="bg-white shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Link href="/" className="flex items-center">
              <span className="text-2xl font-bold text-primary-600">QuickBite</span>
            </Link>
            <div className="hidden md:flex ml-10 space-x-8">
              <Link href="/restaurants" className="text-gray-700 hover:text-primary-600 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                Рестораны
              </Link>
              {isAuthenticated && (
                <>
                  <Link href="/orders" className="text-gray-700 hover:text-primary-600 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                    Мои заказы
                  </Link>
                  <Link href="/notifications" className="text-gray-700 hover:text-primary-600 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                    Уведомления
                  </Link>
                </>
              )}
            </div>
          </div>

          <div className="flex items-center space-x-4">
            {isAuthenticated ? (
              <>
                <Link href="/cart" className="relative">
                  <button className="p-2 text-gray-700 hover:text-primary-600 transition-colors">
                    <ShoppingCart size={24} />
                    {cartCount > 0 && (
                      <span className="absolute -top-1 -right-1 bg-primary-600 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                        {cartCount}
                      </span>
                    )}
                  </button>
                </Link>

                <Link href="/profile">
                  <button className="p-2 text-gray-700 hover:text-primary-600 transition-colors">
                    <User size={24} />
                  </button>
                </Link>

                <button
                  onClick={logout}
                  className="flex items-center gap-2 px-4 py-2 text-gray-700 hover:text-primary-600 transition-colors"
                >
                  <LogOut size={20} />
                  <span className="hidden md:inline">Выйти</span>
                </button>
              </>
            ) : (
              <>
                <button
                  onClick={login}
                  className="px-4 py-2 text-primary-600 hover:text-primary-700 font-medium transition-colors"
                >
                  Войти
                </button>
                <Link href="/auth/register">
                  <button className="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg font-medium transition-colors">
                    Регистрация
                  </button>
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}

