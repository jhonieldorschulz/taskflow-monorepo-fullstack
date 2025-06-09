import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { 
    Card, 
    CreateCardRequest, 
    UpdateCardRequest,
    MoveCardRequest 
} from '../models/card.model';

@Injectable({
    providedIn: 'root'
})
export class CardService extends ApiService {
    getAllByList(listId: number): Observable<Card[]> {
        return this.get<Card[]>(`/api/cards/list/${listId}`);
    }

    getById(id: number): Observable<Card> {
        return this.get<Card>(`/api/cards/${id}`);
    }

    create(request: CreateCardRequest): Observable<Card> {
        return this.post<Card>('/api/cards', request);
    }

    update(id: number, request: UpdateCardRequest): Observable<Card> {
        return this.put<Card>(`/api/cards/${id}`, request);
    }

    delete(id: number): Observable<void> {
        return this.delete<void>(`/api/cards/${id}`);
    }

    moveCard(id: number, request: MoveCardRequest): Observable<Card> {
        return this.put<Card>(`/api/cards/${id}/move`, request);
    }
} 