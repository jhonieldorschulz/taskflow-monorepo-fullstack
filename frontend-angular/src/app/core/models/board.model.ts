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