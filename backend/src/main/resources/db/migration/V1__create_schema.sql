CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE projeto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    descricao TEXT,
    usuario_id INT REFERENCES usuario(id)
);

CREATE TABLE quadro (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    projeto_id INT REFERENCES projeto(id)
);

CREATE TABLE tarefa (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(100),
    descricao TEXT,
    status VARCHAR(20),
    quadro_id INT REFERENCES quadro(id)
);