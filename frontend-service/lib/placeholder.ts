/**
 * Генерирует URL placeholder изображения для еды
 * Использует picsum.photos для красивых изображений
 */
export function getPlaceholderImage(seed?: string, width: number = 400, height: number = 300): string {
  if (seed) {
    // Для конкретного блюда используем ID из picsum
    const id = Math.abs(hashCode(seed)) % 1000;
    return `https://picsum.photos/id/${id}/${width}/${height}`;
  }
  
  // Случайное изображение
  return `https://picsum.photos/${width}/${height}`;
}

/**
 * Генерирует URL изображения еды из Unsplash
 */
export function getFoodImage(query: string = 'food', width: number = 400, height: number = 300): string {
  // Используем picsum.photos - работает без CORS проблем
  const id = Math.abs(hashCode(query)) % 100 + 1;
  return `https://picsum.photos/${width}/${height}?random=${id}`;
}

/**
 * Получить изображение еды с поддержкой fallback
 */
export function getMenuItemImage(imageUrl?: string | null, itemName?: string): string {
  if (imageUrl && !imageUrl.includes('example.com')) {
    return imageUrl;
  }
  
  // Fallback к picsum.photos с seed из названия
  const seed = itemName || 'Food';
  const id = Math.abs(hashCode(seed)) % 100 + 1;
  return `https://picsum.photos/400/300?random=${id}`;
}

/**
 * Простая hash функция для генерации консистентных ID
 */
function hashCode(str: string): number {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    const char = str.charCodeAt(i);
    hash = ((hash << 5) - hash) + char;
    hash = hash & hash;
  }
  return hash;
}
