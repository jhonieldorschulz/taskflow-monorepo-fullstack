import api from './api';
import { Card } from './taskListService';

export interface CreateCardRequest {
    title: string;
    description?: string;
    listId: number;
    position?: number;
    assigneeId?: number;
    dueDate?: string;
    priority?: string;
    labels?: string[];
}

export interface UpdateCardRequest {
    title?: string;
    description?: string;
    position?: number;
    listId?: number;
    assigneeId?: number;
    dueDate?: string;
    priority?: string;
    labels?: string[];
}

const cardService = {
    getAllByList: async (listId: number): Promise<Card[]> => {
        const response = await api.get(`/api/cards/list/${listId}`);
        return response.data;
    },

    getById: async (id: number): Promise<Card> => {
        const response = await api.get(`/api/cards/${id}`);
        return response.data;
    },

    create: async (request: CreateCardRequest): Promise<Card> => {
        const response = await api.post('/api/cards', request);
        return response.data;
    },

    update: async (id: number, request: UpdateCardRequest): Promise<Card> => {
        const response = await api.put(`/api/cards/${id}`, request);
        return response.data;
    },

    delete: async (id: number): Promise<void> => {
        await api.delete(`/api/cards/${id}`);
    },

    moveCard: async (id: number, listId: number, position: number): Promise<Card> => {
        const response = await api.put(`/api/cards/${id}/move`, {
            listId,
            position
        });
        return response.data;
    }
};

export default cardService; 