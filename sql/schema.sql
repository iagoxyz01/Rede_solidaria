CREATE DATABASE IF NOT EXISTS rede_solidaria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE rede_solidaria;

CREATE TABLE doador (
 id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(120) NOT NULL, contato VARCHAR(80) NOT NULL,
 tipo ENUM('CIDADAO','ORGANIZACAO') NOT NULL, cpf VARCHAR(14) UNIQUE,
 nome_instituicao VARCHAR(150), cnpj VARCHAR(18) UNIQUE
);
CREATE TABLE familia_acolhida (
 id INT AUTO_INCREMENT PRIMARY KEY, nome_responsavel VARCHAR(120) NOT NULL, contato VARCHAR(80),
 quantidade_pessoas INT NOT NULL, nivel_urgencia ENUM('BAIXA','MEDIA','ALTA') NOT NULL,
 data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, ativo BOOLEAN NOT NULL DEFAULT TRUE,
 CHECK (quantidade_pessoas > 0)
);
CREATE TABLE produto (
 id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(120) NOT NULL,
 categoria ENUM('ALIMENTO','ROUPA','HIGIENE') NOT NULL, variacao VARCHAR(120), ativo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE estoque (
 id INT AUTO_INCREMENT PRIMARY KEY, produto_id INT NOT NULL UNIQUE,
 quantidade_disponivel INT NOT NULL DEFAULT 0, FOREIGN KEY (produto_id) REFERENCES produto(id),
 CHECK (quantidade_disponivel >= 0)
);
CREATE TABLE doacao (
 id INT AUTO_INCREMENT PRIMARY KEY, doador_id INT NOT NULL, produto_id INT NOT NULL,
 quantidade_recebida INT NOT NULL, validade DATE, data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 estado_aptidao ENUM('APTA','BLOQUEADA','IMPROPRIA') NOT NULL DEFAULT 'APTA',
 FOREIGN KEY (doador_id) REFERENCES doador(id), FOREIGN KEY (produto_id) REFERENCES produto(id),
 CHECK (quantidade_recebida > 0)
);
CREATE TABLE necessidade_familia (
 id INT AUTO_INCREMENT PRIMARY KEY, familia_id INT NOT NULL, produto_id INT NOT NULL,
 quantidade_necessaria INT NOT NULL, quantidade_atendida INT NOT NULL DEFAULT 0,
 status ENUM('ABERTA','CONCLUIDA','ENCERRADA') NOT NULL DEFAULT 'ABERTA',
 FOREIGN KEY (familia_id) REFERENCES familia_acolhida(id), FOREIGN KEY (produto_id) REFERENCES produto(id),
 CHECK (quantidade_necessaria > 0), CHECK (quantidade_atendida >= 0)
);
CREATE TABLE inspecao_doacao (
 id INT AUTO_INCREMENT PRIMARY KEY, doacao_id INT NOT NULL, data_inspecao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 resultado ENUM('APTA','BLOQUEADA','IMPROPRIA') NOT NULL, motivo VARCHAR(255),
 FOREIGN KEY (doacao_id) REFERENCES doacao(id)
);
CREATE TABLE liberacao_doacao (
 id INT AUTO_INCREMENT PRIMARY KEY, doacao_id INT NOT NULL, data_liberacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 justificativa VARCHAR(255), FOREIGN KEY (doacao_id) REFERENCES doacao(id)
);
CREATE TABLE destinacao (
 id INT AUTO_INCREMENT PRIMARY KEY, necessidade_id INT NOT NULL, doacao_id INT NOT NULL, quantidade INT NOT NULL,
 data_reserva DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, data_conclusao DATETIME, data_cancelamento DATETIME,
 motivo_cancelamento VARCHAR(255), status ENUM('RESERVADA','CONCLUIDA','CANCELADA') NOT NULL DEFAULT 'RESERVADA',
 FOREIGN KEY (necessidade_id) REFERENCES necessidade_familia(id), FOREIGN KEY (doacao_id) REFERENCES doacao(id),
 CHECK (quantidade > 0)
);