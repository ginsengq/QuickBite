import { userServiceApi } from '../api-client';
import { User, CreateUserRequest } from '@/types';

export const userService = {
  async createUser(data: CreateUserRequest): Promise<User> {
    return userServiceApi.post<User>('/api/users', data);
  },

  async getUser(id: number): Promise<User> {
    return userServiceApi.get<User>(`/api/users/${id}`);
  },

  async getAllUsers(): Promise<User[]> {
    return userServiceApi.get<User[]>('/api/users');
  },

  async updateUser(id: number, data: Partial<User>): Promise<User> {
    return userServiceApi.put<User>(`/api/users/${id}`, data);
  },

  async deleteUser(id: number): Promise<void> {
    return userServiceApi.delete<void>(`/api/users/${id}`);
  },
};

