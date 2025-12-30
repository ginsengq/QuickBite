'use client';

import { Restaurant } from '@/types';
import Link from 'next/link';
import { Star, MapPin, Phone } from 'lucide-react';
import { getMenuItemImage } from '@/lib/placeholder';

interface RestaurantCardProps {
  restaurant: Restaurant;
}

export default function RestaurantCard({ restaurant }: RestaurantCardProps) {
  return (
    <Link href={`/restaurants/${restaurant.id}`}>
      <div className="card hover:shadow-xl cursor-pointer transition-all">
        <img
          src={getMenuItemImage(restaurant.imageUrl, restaurant.name)}
          alt={restaurant.name}
          className="w-full h-48 object-cover rounded-t-lg -mt-6 -mx-6 mb-4"
          onError={(e) => {
            const id = Math.abs(restaurant.id.hashCode()) % 100 + 1;
            e.currentTarget.src = `https://picsum.photos/400/300?random=${id}`;
          }}
        />

        <h3 className="text-xl font-semibold mb-2">{restaurant.name}</h3>

        {restaurant.rating && (
          <div className="flex items-center gap-1 mb-2">
            <Star className="text-yellow-400 fill-current" size={18} />
            <span className="font-medium">{restaurant.rating.toFixed(1)}</span>
          </div>
        )}

        <div className="flex items-start gap-2 text-gray-600 text-sm mb-2">
          <MapPin size={16} className="mt-0.5 flex-shrink-0" />
          <span>{restaurant.address}</span>
        </div>

        <div className="flex items-center gap-2 text-gray-600 text-sm">
          <Phone size={16} className="flex-shrink-0" />
          <span>{restaurant.phone}</span>
        </div>

        {restaurant.description && (
          <p className="text-gray-600 text-sm mt-3 line-clamp-2">
            {restaurant.description}
          </p>
        )}
      </div>
    </Link>
  );
}

