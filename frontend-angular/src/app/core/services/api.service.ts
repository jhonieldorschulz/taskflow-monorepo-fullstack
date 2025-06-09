import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    private baseUrl = environment.apiUrl;

    constructor(private http: HttpClient) {}

    protected getHeaders(): HttpHeaders {
        const token = localStorage.getItem('token');
        let headers = new HttpHeaders()
            .set('Content-Type', 'application/json');
        
        if (token) {
            headers = headers.set('Authorization', `Bearer ${token}`);
        }
        
        return headers;
    }

    protected get<T>(path: string) {
        return this.http.get<T>(`${this.baseUrl}${path}`, {
            headers: this.getHeaders()
        });
    }

    protected post<T>(path: string, body: any) {
        return this.http.post<T>(`${this.baseUrl}${path}`, body, {
            headers: this.getHeaders()
        });
    }

    protected put<T>(path: string, body: any) {
        return this.http.put<T>(`${this.baseUrl}${path}`, body, {
            headers: this.getHeaders()
        });
    }

    protected delete<T>(path: string) {
        return this.http.delete<T>(`${this.baseUrl}${path}`, {
            headers: this.getHeaders()
        });
    }
} 