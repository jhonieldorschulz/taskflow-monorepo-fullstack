import { Card } from './card.model';

export interface TaskList {
    id: number;
    name: string;
    position: number;
    boardId: number;
    createdAt: string;
    cards: Card[];
}

export interface CreateTaskListRequest {
    name: string;
    boardId: number;
}

export interface UpdateTaskListRequest {
    name?: string;
    position?: number;
} 