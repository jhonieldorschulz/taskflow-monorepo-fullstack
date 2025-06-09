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

export interface MoveCardRequest {
    listId: number;
    position: number;
} 