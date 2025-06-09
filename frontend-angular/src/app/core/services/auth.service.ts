import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { ApiService } from './api.service';
import { AuthResponse, LoginRequest, RegisterRequest, User } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService extends ApiService {
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor() {
        super();
        this.loadStoredUser();
    }

    private loadStoredUser(): void {
        const userStr = localStorage.getItem('user');
        if (userStr) {
            this.currentUserSubject.next(JSON.parse(userStr));
        }
    }

    login(request: LoginRequest): Observable<AuthResponse> {
        return this.post<AuthResponse>('/api/auth/login', request).pipe(
            tap(response => this.handleAuthResponse(response))
        );
    }

    register(request: RegisterRequest): Observable<AuthResponse> {
        return this.post<AuthResponse>('/api/auth/register', request).pipe(
            tap(response => this.handleAuthResponse(response))
        );
    }

    logout(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        this.currentUserSubject.next(null);
    }

    private handleAuthResponse(response: AuthResponse): void {
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response.user));
        this.currentUserSubject.next(response.user);
    }

    isAuthenticated(): boolean {
        return !!this.currentUserSubject.value;
    }

    getCurrentUser(): User | null {
        return this.currentUserSubject.value;
    }
} 