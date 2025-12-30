import Keycloak from 'keycloak-js';

const keycloakConfig = {
  url: process.env.NEXT_PUBLIC_KEYCLOAK_URL || 'http://localhost:8082',
  realm: process.env.NEXT_PUBLIC_KEYCLOAK_REALM || 'quickbite',
  clientId: process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID || 'backend-api',
};

// Инициализируем Keycloak только на клиенте
let keycloak: Keycloak | null = null;

if (typeof window !== 'undefined') {
  keycloak = new Keycloak(keycloakConfig);
}

export default keycloak!;


