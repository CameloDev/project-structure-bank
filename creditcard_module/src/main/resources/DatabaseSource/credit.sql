CREATE DATABASE card_dev;
USE card_dev;

-- Tabela principal de cartões
CREATE TABLE cartoes (
                         cartao_id INT AUTO_INCREMENT PRIMARY KEY,
                         cliente_id INT, -- NOT NULL  -- Referência ao ID do cliente do modulo Account
                         numero VARCHAR(16) UNIQUE NOT NULL,
                         senha VARCHAR(6) NOT NULL,
                         nome_impresso VARCHAR(100) NOT NULL,
                         cvv VARCHAR(4) NOT NULL,
                         data_validade DATE NOT NULL,
                         limite_total DECIMAL(15,2) NOT NULL,
                         limite_disponivel DECIMAL(15,2) NOT NULL,
                         bandeira ENUM('visa', 'mastercard', 'elo', 'hipercard') NOT NULL,
                         status ENUM('ativo', 'bloqueado', 'cancelado') DEFAULT 'ativo',
                         aprovacao_automatica BOOLEAN DEFAULT TRUE,
                         eh_adicional BOOLEAN DEFAULT FALSE,
                         data_emissao DATE NOT NULL
);

-- Tabela de transações apenas como teste
CREATE TABLE transacoes (
                            transacao_id INT AUTO_INCREMENT PRIMARY KEY,
                            cartao_id INT NOT NULL,
                            valor DECIMAL(15,2) NOT NULL,
                            data_transacao DATETIME DEFAULT CURRENT_TIMESTAMP,
                            estabelecimento VARCHAR(100),
                            categoria ENUM('alimentacao', 'transporte', 'lazer', 'compras', 'outros') NOT NULL,
                            status ENUM('pendente', 'aprovada', 'negada', 'estornada') DEFAULT 'pendente',
                            FOREIGN KEY (cartao_id) REFERENCES cartoes(cartao_id)
);

-- Tabela de faturas
CREATE TABLE faturas (
                         fatura_id INT AUTO_INCREMENT PRIMARY KEY,
                         cartao_id INT NOT NULL,
                         valor_total DECIMAL(15,2) NOT NULL,
                         data_vencimento DATE NOT NULL,
                         data_fechamento DATE NOT NULL,
                         status ENUM('aberta', 'paga', 'atrasada', 'parcelada') DEFAULT 'aberta',
                         FOREIGN KEY (cartao_id) REFERENCES cartoes(cartao_id)
);

-- Tabela de pagamentos apenas como teste
CREATE TABLE pagamentos (
                            pagamento_id INT AUTO_INCREMENT PRIMARY KEY,
                            fatura_id INT NOT NULL,
                            valor DECIMAL(15,2) NOT NULL,
                            data_pagamento DATETIME DEFAULT CURRENT_TIMESTAMP,
                            metodo ENUM('boleto', 'transferencia', 'pix', 'debito_conta') NOT NULL,
                            status ENUM('pendente', 'confirmado', 'cancelado') DEFAULT 'pendente',
                            FOREIGN KEY (fatura_id) REFERENCES faturas(fatura_id)
);

-- Tabela de histórico de alterações do cartão
CREATE TABLE historico_cartoes (
                                   historico_id INT AUTO_INCREMENT PRIMARY KEY,
                                   cartao_id INT NOT NULL,
                                   acao ENUM('bloqueio', 'desbloqueio', 'ajuste_limite', 'alteracao_status') NOT NULL,
                                   detalhes TEXT,
                                   data_alteracao DATETIME DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (cartao_id) REFERENCES cartoes(cartao_id)
);