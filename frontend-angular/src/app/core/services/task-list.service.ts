import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { 
    TaskList, 
    CreateTaskListRequest, 
    UpdateTaskListRequest 
} from '../models/task-list.model';

@Injectable({
    providedIn: 'root'
})
export class TaskListService extends ApiService {
    getAllByBoard(boardId: number): Observable<TaskList[]> {
        return this.get<TaskList[]>(`/api/tasklists/board/${boardId}`);
    }

    getById(id: number): Observable<TaskList> {
        return this.get<TaskList>(`/api/tasklists/${id}`);
    }

    create(request: CreateTaskListRequest): Observable<TaskList> {
        return this.post<TaskList>('/api/tasklists', request);
    }

    update(id: number, request: UpdateTaskListRequest): Observable<TaskList> {
        return this.put<TaskList>(`/api/tasklists/${id}`, request);
    }

    delete(id: number): Observable<void> {
        return this.delete<void>(`/api/tasklists/${id}`);
    }
} 