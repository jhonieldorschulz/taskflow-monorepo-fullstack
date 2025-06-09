import api from './api';

export interface User {
    id: number;
    name: string;
    email: string;
    createdAt: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    name: string;
    email: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    user: User;
}

const authService = {
    login: async (request: LoginRequest): Promise<AuthResponse> => {
        const response = await api.post('/api/auth/login', request);
        const { token, user } = response.data;
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
        return response.data;
    },

    register: async (request: RegisterRequest): Promise<AuthResponse> => {
        const response = await api.post('/api/auth/register', request);
        const { token, user } = response.data;
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
        return response.data;
    },

    logout: (): void => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },

    getCurrentUser: (): User | null => {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },

    isAuthenticated: (): boolean => {
        return !!localStorage.getItem('token');
    },

    getToken: (): string | null => {
        return localStorage.getItem('token');
    }
};

export default authService; 