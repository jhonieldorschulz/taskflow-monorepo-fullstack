import api from './api';

export interface Board {
    id: number;
    name: string;
    description?: string;
    ownerId: number;
    createdAt: string;
    members?: BoardMember[];
}

export interface BoardMember {
    id: number;
    userId: number;
    boardId: number;
    role: 'OWNER' | 'ADMIN' | 'MEMBER';
    createdAt: string;
    user: {
        id: number;
        name: string;
        email: string;
    };
}

export interface CreateBoardRequest {
    name: string;
    description?: string;
}

export interface UpdateBoardRequest {
    name?: string;
    description?: string;
}

export interface AddMemberRequest {
    userId: number;
    role: 'ADMIN' | 'MEMBER';
}

const boardService = {
    getAll: async (): Promise<Board[]> => {
        const response = await api.get('/api/boards');
        return response.data;
    },

    getById: async (id: number): Promise<Board> => {
        const response = await api.get(`/api/boards/${id}`);
        return response.data;
    },

    create: async (request: CreateBoardRequest): Promise<Board> => {
        const response = await api.post('/api/boards', request);
        return response.data;
    },

    update: async (id: number, request: UpdateBoardRequest): Promise<Board> => {
        const response = await api.put(`/api/boards/${id}`, request);
        return response.data;
    },

    delete: async (id: number): Promise<void> => {
        await api.delete(`/api/boards/${id}`);
    },

    addMember: async (boardId: number, request: AddMemberRequest): Promise<BoardMember> => {
        const response = await api.post(`/api/boards/${boardId}/members`, request);
        return response.data;
    },

    removeMember: async (boardId: number, userId: number): Promise<void> => {
        await api.delete(`/api/boards/${boardId}/members/${userId}`);
    },

    updateMemberRole: async (boardId: number, userId: number, role: 'ADMIN' | 'MEMBER'): Promise<BoardMember> => {
        const response = await api.put(`/api/boards/${boardId}/members/${userId}`, { role });
        return response.data;
    }
};

export default boardService; 