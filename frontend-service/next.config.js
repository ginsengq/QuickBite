/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  output: 'standalone',
  env: {
    KEYCLOAK_URL: process.env.KEYCLOAK_URL || 'http://localhost:8082',
    KEYCLOAK_REALM: process.env.KEYCLOAK_REALM || 'quickbite',
    KEYCLOAK_CLIENT_ID: process.env.KEYCLOAK_CLIENT_ID || 'backend-api',
    ORDER_SERVICE_URL: process.env.ORDER_SERVICE_URL || 'http://localhost:8080',
    RESTAURANT_SERVICE_URL: process.env.RESTAURANT_SERVICE_URL || 'http://localhost:8081',
    USER_SERVICE_URL: process.env.USER_SERVICE_URL || 'http://localhost:8083',
    PAYMENT_SERVICE_URL: process.env.PAYMENT_SERVICE_URL || 'http://localhost:8084',
    NOTIFICATION_SERVICE_URL: process.env.NOTIFICATION_SERVICE_URL || 'http://localhost:8085',
  },
  images: {
    domains: ['localhost'],
  },
}

module.exports = nextConfig
