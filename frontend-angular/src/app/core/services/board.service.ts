import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { 
    Board, 
    BoardMember, 
    CreateBoardRequest, 
    UpdateBoardRequest, 
    AddMemberRequest 
} from '../models/board.model';

@Injectable({
    providedIn: 'root'
})
export class BoardService extends ApiService {
    getAll(): Observable<Board[]> {
        return this.get<Board[]>('/api/boards');
    }

    getById(id: number): Observable<Board> {
        return this.get<Board>(`/api/boards/${id}`);
    }

    create(request: CreateBoardRequest): Observable<Board> {
        return this.post<Board>('/api/boards', request);
    }

    update(id: number, request: UpdateBoardRequest): Observable<Board> {
        return this.put<Board>(`/api/boards/${id}`, request);
    }

    delete(id: number): Observable<void> {
        return this.delete<void>(`/api/boards/${id}`);
    }

    addMember(boardId: number, request: AddMemberRequest): Observable<BoardMember> {
        return this.post<BoardMember>(`/api/boards/${boardId}/members`, request);
    }

    removeMember(boardId: number, userId: number): Observable<void> {
        return this.delete<void>(`/api/boards/${boardId}/members/${userId}`);
    }

    updateMemberRole(boardId: number, userId: number, role: 'ADMIN' | 'MEMBER'): Observable<BoardMember> {
        return this.put<BoardMember>(`/api/boards/${boardId}/members/${userId}`, { role });
    }
} 