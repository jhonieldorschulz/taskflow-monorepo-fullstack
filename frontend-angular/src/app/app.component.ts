import { Component, OnInit } from '@angular/core';
import { TaskService } from './task.service';

@Component({
  selector: 'app-root',
  template: \`
    <h1>Projetos</h1>
    <ul>
      <li *ngFor="let projeto of projetos">{{ projeto.nome }}</li>
    </ul>

    <h2>Usuários Externos</h2>
    <ul>
      <li *ngFor="let usuario of usuarios">{{ usuario.name }} - {{ usuario.email }}</li>
    </ul>
  \`
})
export class AppComponent implements OnInit {
  projetos: any[] = [];
  usuarios: any[] = [];

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.taskService.getProjetos().subscribe(data => this.projetos = data);
    this.taskService.getUsuariosExternos().subscribe(data => this.usuarios = data);
  }
}