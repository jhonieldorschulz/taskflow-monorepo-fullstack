import api from './api';

export interface TaskList {
    id: number;
    name: string;
    position: number;
    boardId: number;
    createdAt: string;
    cards: Card[];
}

export interface Card {
    id: number;
    title: string;
    description: string;
    position: number;
    listId: number;
    assignee?: {
        id: number;
        email: string;
        name: string;
        createdAt: string;
    };
    dueDate?: string;
    priority?: string;
    labels?: string[];
    createdAt: string;
}

export interface CreateTaskListRequest {
    name: string;
    boardId: number;
}

export interface UpdateTaskListRequest {
    name?: string;
    position?: number;
}

const taskListService = {
    getAllByBoard: async (boardId: number): Promise<TaskList[]> => {
        const response = await api.get(`/api/tasklists/board/${boardId}`);
        return response.data;
    },

    getById: async (id: number): Promise<TaskList> => {
        const response = await api.get(`/api/tasklists/${id}`);
        return response.data;
    },

    create: async (request: CreateTaskListRequest): Promise<TaskList> => {
        const response = await api.post('/api/tasklists', request);
        return response.data;
    },

    update: async (id: number, request: UpdateTaskListRequest): Promise<TaskList> => {
        const response = await api.put(`/api/tasklists/${id}`, request);
        return response.data;
    },

    delete: async (id: number): Promise<void> => {
        await api.delete(`/api/tasklists/${id}`);
    }
};

export default taskListService; 