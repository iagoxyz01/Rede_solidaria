package com.RedeSolidaria.services;

import com.RedeSolidaria.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoadorService {

    public int cadastrarDoador(String nome, String contato, String tipo,
                               String cpf, String nomeInstituicao, String cnpj) throws SQLException {
        String sql = "INSERT INTO doador (nome, contato, tipo, cpf, nome_instituicao, cnpj) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setString(2, contato);
            ps.setString(3, tipo);
            ps.setString(4, vazioParaNull(cpf));
            ps.setString(5, vazioParaNull(nomeInstituicao));
            ps.setString(6, vazioParaNull(cnpj));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Não foi possível obter o ID do doador.");
    }

    public List<String> listarNecessidades() throws SQLException {
        String sql = "SELECT n.id, f.nome_responsavel, p.nome AS produto, p.categoria, "
                   + "n.quantidade_necessaria, n.quantidade_atendida, "
                   + "(n.quantidade_necessaria - n.quantidade_atendida) AS pendente, "
                   + "f.nivel_urgencia "
                   + "FROM necessidade_familia n "
                   + "JOIN familia_acolhida f ON f.id = n.familia_id "
                   + "JOIN produto p ON p.id = n.produto_id "
                   + "WHERE n.status = 'ABERTA' AND f.ativo = TRUE "
                   + "AND n.quantidade_necessaria > n.quantidade_atendida "
                   + "ORDER BY FIELD(f.nivel_urgencia, 'ALTA', 'MEDIA', 'BAIXA'), f.data_cadastro";
        List<String> linhas = new ArrayList<>();
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                linhas.add(String.format(
                    "Necessidade #%d | Família: %s | Produto: %s (%s) | Pendente: %d | Urgência: %s",
                    rs.getInt("id"), rs.getString("nome_responsavel"),
                    rs.getString("produto"), rs.getString("categoria"),
                    rs.getInt("pendente"), rs.getString("nivel_urgencia")));
            }
        }
        return linhas;
    }

    public int registrarDoacao(int doadorId, String nomeProduto, String categoria,
                               String variacao, int quantidade, Date validade,
                               Boolean prontoConsumo, Boolean lacrado, String estacao) throws SQLException {
        String tipo = categoria.toUpperCase();
        if (!tipo.equals("ALIMENTO") && !tipo.equals("ROUPA") && !tipo.equals("HIGIENE")) {
            throw new IllegalArgumentException("Categoria inválida.");
        }
        if (quantidade <= 0) throw new IllegalArgumentException("A quantidade deve ser maior que zero.");

        String inserirProduto = "INSERT INTO produto (nome, categoria, variacao) VALUES (?, ?, ?)";
        String buscarProduto = "SELECT id FROM produto WHERE nome = ? AND categoria = ? "
                             + "AND ((variacao IS NULL AND ? IS NULL) OR variacao = ?) LIMIT 1";
        String inserirDoacao = "INSERT INTO doacao "
            + "(doador_id, produto_id, tipo, quantidade_recebida, validade, estado_aptidao, "
            + "pronto_consumo, lacrado, estacao) VALUES (?, ?, ?, ?, ?, 'APTA', ?, ?, ?)";
        String atualizarEstoque = "INSERT INTO estoque (produto_id, quantidade_disponivel) VALUES (?, ?) "
            + "ON DUPLICATE KEY UPDATE quantidade_disponivel = quantidade_disponivel + VALUES(quantidade_disponivel)";

        try (Connection c = Conexao.conectar()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement("SELECT id FROM doador WHERE id = ?")) {
                    ps.setInt(1, doadorId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Doador não encontrado. Confira o ID.");
                    }
                }

                int produtoId = -1;
                try (PreparedStatement ps = c.prepareStatement(buscarProduto)) {
                    ps.setString(1, nomeProduto);
                    ps.setString(2, tipo);
                    ps.setString(3, vazioParaNull(variacao));
                    ps.setString(4, vazioParaNull(variacao));
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) produtoId = rs.getInt("id");
                    }
                }

                if (produtoId == -1) {
                    try (PreparedStatement ps = c.prepareStatement(inserirProduto, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, nomeProduto);
                        ps.setString(2, tipo);
                        ps.setString(3, vazioParaNull(variacao));
                        ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (!rs.next()) throw new SQLException("Não foi possível criar o produto.");
                            produtoId = rs.getInt(1);
                        }
                    }
                }

                int doacaoId;
                try (PreparedStatement ps = c.prepareStatement(inserirDoacao, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, doadorId);
                    ps.setInt(2, produtoId);
                    ps.setString(3, tipo);
                    ps.setInt(4, quantidade);
                    if (validade == null) ps.setNull(5, Types.DATE); else ps.setDate(5, validade);
                    if (prontoConsumo == null) ps.setNull(6, Types.BOOLEAN); else ps.setBoolean(6, prontoConsumo);
                    if (lacrado == null) ps.setNull(7, Types.BOOLEAN); else ps.setBoolean(7, lacrado);
                    ps.setString(8, vazioParaNull(estacao));
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new SQLException("Não foi possível registrar a doação.");
                        doacaoId = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps = c.prepareStatement(atualizarEstoque)) {
                    ps.setInt(1, produtoId);
                    ps.setInt(2, quantidade);
                    ps.executeUpdate();
                }

                c.commit();
                return doacaoId;
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<String> listarHistorico(int doadorId) throws SQLException {
        String sql = "SELECT d.id, p.nome AS produto, d.tipo, d.quantidade_recebida, "
            + "d.data_cadastro, d.estado_aptidao, "
            + "(SELECT i.resultado FROM inspecao_doacao i WHERE i.doacao_id = d.id "
            + "ORDER BY i.data_inspecao DESC, i.id DESC LIMIT 1) AS ultima_inspecao, "
            + "(SELECT GROUP_CONCAT(DISTINCT dt.status ORDER BY dt.status SEPARATOR ', ') "
            + "FROM destinacao dt WHERE dt.doacao_id = d.id) AS destinacoes "
            + "FROM doacao d JOIN produto p ON p.id = d.produto_id "
            + "WHERE d.doador_id = ? ORDER BY d.data_cadastro DESC";
        List<String> linhas = new ArrayList<>();
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doadorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String inspecao = rs.getString("ultima_inspecao");
                    String destinacoes = rs.getString("destinacoes");
                    linhas.add(String.format(
                        "Doação #%d | %s | %s | Quantidade: %d | Data: %s | Aptidão: %s | Inspeção: %s | Destinação: %s",
                        rs.getInt("id"), rs.getString("produto"), rs.getString("tipo"),
                        rs.getInt("quantidade_recebida"), rs.getTimestamp("data_cadastro"),
                        rs.getString("estado_aptidao"),
                        inspecao == null ? "Ainda não registrada" : inspecao,
                        destinacoes == null ? "Ainda não destinada" : destinacoes));
                }
            }
        }
        return linhas;
    }

    private String vazioParaNull(String valor) {
        return valor == null || valor.trim().isEmpty() ? null : valor.trim();
    }
}
