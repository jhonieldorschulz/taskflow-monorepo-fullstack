import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TaskService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getProjetos(): Observable<any> {
    return this.http.get(`${this.apiUrl}/projetos`);
  }

  getUsuariosExternos(): Observable<any> {
    return this.http.get(`${this.apiUrl}/external/usuarios`);
  }
}
