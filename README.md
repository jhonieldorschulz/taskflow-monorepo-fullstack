# 🧩 TaskFlow Mono-Repo — Sistema de Gerenciamento de Tarefas

## 📦 Tecnologias
- **Backend:** Spring Boot + JPA + PostgreSQL + Flyway + OpenFeign
- **Frontend Angular:** Angular + Material (modo SPA)
- **Frontend React:** Next.js + Tailwind (modo SSR)
- **Infraestrutura:** Docker + Docker Compose
- **Gerenciamento:** Swagger + pgAdmin

---

## 🚀 Como rodar tudo com Docker Compose

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/taskflow-mono-repo.git
cd taskflow-mono-repo
```

### 2. Suba os containers
```bash
docker-compose up --build
```

---

## 🌐 Endpoints dos Serviços

| Serviço         | URL                                |
|----------------|-------------------------------------|
| API Backend     | http://localhost:8080              |
| Swagger UI      | http://localhost:8080/swagger-ui.html |
| Frontend Angular| http://localhost:4200              |
| Frontend React  | http://localhost:3000              |
| pgAdmin         | http://localhost:8081              |

- **pgAdmin login:** `admin@taskflow.com` / `admin`
- **Banco PostgreSQL:** `taskflow` | usuário: `taskadmin` | senha: `taskpass`

---

## 🔗 API REST Endpoints

### Projetos
- `GET /api/projetos`
- `POST /api/projetos`
- `PUT /api/projetos/{id}`
- `DELETE /api/projetos/{id}`

### Quadros
- `GET /api/quadros`
- `POST /api/quadros`

### Tarefas
- `GET /api/tarefas`
- `POST /api/tarefas`
- `PUT /api/tarefas/{id}`
- `DELETE /api/tarefas/{id}`

### API Externa (via OpenFeign)
- `GET /api/external/usuarios` → retorna dados do https://jsonplaceholder.typicode.com/users

---

## 📘 Como testar

1. Acesse o Swagger UI: `http://localhost:8080/swagger-ui.html`
2. Teste endpoints diretamente por ali
3. Acesse os frontends Angular e React para ver os dados sendo exibidos
4. Use o pgAdmin para explorar o banco de dados

---

## 🧠 Objetivo didático

Este projeto foi criado para demonstrar na prática:

- Criação de APIs REST em Spring Boot
- Integração com API externa usando OpenFeign
- Versionamento de banco com Flyway
- Consumo da API em frontends Angular e React
- Orquestração completa com Docker

Tudo num único mono-repo para facilitar o aprendizado e deploy.

---

**Autor:** Prof. Jhoni • Desenvolvido com 💡 e café ☕