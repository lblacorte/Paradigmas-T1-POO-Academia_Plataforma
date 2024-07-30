CREATE DATABASE IF NOT EXISTS academia
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

\c academia;

CREATE TABLE IF NOT EXISTS Alunos(
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL
);

INSERT INTO Alunos (cpf, nome, data_nascimento) VALUES
('123.456.789-01', 'Fulano', '1976-01-20'),
('234.567.890-12', 'Ciclano', '2000-01-01');

CREATE TABLE IF NOT EXISTS Planos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    preco NUMERIC(5,2) NOT NULL
);

INSERT INTO Planos (nome, preco) VALUES
('Simples', 79.90),
('Gold', 89.90),
('Premium', 99.90);

CREATE TABLE IF NOT EXISTS Exercicios (
    id INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    musculos_trabalhados VARCHAR(255) NOT NULL
);

INSERT INTO Exercicios (id, nome, musculos_trabalhados) VALUES
(1, 'Leg Press', 'Quadríceps, Glúteos'),
(5, 'Cadeira Adutora', 'Adutores'),
(20, 'Supino Máquina', 'Peitorais, Tríceps'),
(26, 'Crucifixo Máquina', 'Peitorais'),
(40, 'Abdominal Máquina', 'Abdominais'),
(50, 'Desenvolvimento Máquina Aberto', 'Deltoides, Trapézio, Tríceps');

CREATE TABLE IF NOT EXISTS Aluno_Planos (
    id SERIAL PRIMARY KEY,
    cpf_aluno VARCHAR(14) REFERENCES Alunos(cpf),
    id_plano INT REFERENCES Planos(id),
    data_inicio DATE NOT NULL,
    numero_cartao_credito VARCHAR(20) NOT NULL,
    vencimento_cartao_credito DATE NOT NULL,
    cvv_cartao_credito VARCHAR(4) NOT NULL
);

CREATE TABLE IF NOT EXISTS Treinos (
    id SERIAL PRIMARY KEY,
    cpf_aluno VARCHAR(14) REFERENCES Alunos(cpf),
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Treino_Exercicios (
    id SERIAL PRIMARY KEY,
    id_treino INT REFERENCES Treinos(id),
    id_exercicio INT REFERENCES Exercicios(id),
    numero_series INT NOT NULL,
    repeticoes_min INT NOT NULL,
    repeticoes_max INT NOT NULL,
    carga_kg DECIMAL(5, 2) NOT NULL,
    tempo_descanso_min DECIMAL(3, 1) NOT NULL
);

CREATE TABLE IF NOT EXISTS Historico_Treinos_Realizados (
    id SERIAL PRIMARY KEY,
    id_treino INT REFERENCES Treinos(id),
    data_treino DATE NOT NULL,
    treino_concluido BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS Historico_Exercicios_Realizados (
    id SERIAL PRIMARY KEY,
    id_treino_realizado INT REFERENCES Historico_Treinos_Realizados(id),
    id_exercicio INT REFERENCES Treino_Exercicios(id),
    acrescimo_peso DECIMAL(5, 2) DEFAULT 0,
    exercicio_concluido BOOLEAN DEFAULT FALSE
);