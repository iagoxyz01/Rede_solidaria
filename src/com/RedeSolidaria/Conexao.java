package com.RedeSolidaria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Conexao com o MySQL e repositorios de persistencia do Rede Solidaria.
 *
 * Configure as variaveis de ambiente DB_URL, DB_USER e DB_PASSWORD.
 * Padrao: jdbc:mysql://localhost:3306/rede_solidaria?useSSL=false&serverTimezone=America/Sao_Paulo
 * O MySQL Connector/J deve estar disponivel no classpath ao executar o projeto.
 */
public final class Conexao {

    private static final String URL_PADRAO =
            "jdbc:mysql://localhost:3306/rede_solidaria?useSSL=false&serverTimezone=America/Sao_Paulo";

    private Conexao() {
    }

    public static Connection conectar() throws SQLException {
        String url = System.getenv().getOrDefault("DB_URL", URL_PADRAO);
        String usuario = System.getenv().getOrDefault("DB_USER", "root");
        String senha = System.getenv().getOrDefault("DB_PASSWORD", "");
        return DriverManager.getConnection(url, usuario, senha);
    }

    public static boolean testarConexao() {
        try (Connection conexao = conectar()) {
            return conexao.isValid(5);
        } catch (SQLException e) {
            System.err.println("Falha ao conectar ao MySQL: " + e.getMessage());
            return false;
        }
    }

    private abstract static class RepositorioBase {

        protected int inserir(String sql, Object... parametros) throws SQLException {
            try (Connection c = conectar();
                 PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preencher(ps, parametros);
                ps.executeUpdate();
                try (ResultSet chaves = ps.getGeneratedKeys()) {
                    if (chaves.next()) {
                        return chaves.getInt(1);
                    }
                }
                return 0;
            }
        }

        protected int atualizar(String sql, Object... parametros) throws SQLException {
            try (Connection c = conectar();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                preencher(ps, parametros);
                return ps.executeUpdate();
            }
        }

        protected List<Map<String, Object>> consultar(String sql, Object... parametros)
                throws SQLException {
            List<Map<String, Object>> registros = new ArrayList<>();
            try (Connection c = conectar();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                preencher(ps, parametros);
                try (ResultSet rs = ps.executeQuery()) {
                    ResultSetMetaData metadados = rs.getMetaData();
                    int colunas = metadados.getColumnCount();
                    while (rs.next()) {
                        Map<String, Object> registro = new LinkedHashMap<>();
                        for (int i = 1; i <= colunas; i++) {
                            registro.put(metadados.getColumnLabel(i), rs.getObject(i));
                        }
                        registros.add(registro);
                    }
                }
            }
            return registros;
        }

        private void preencher(PreparedStatement ps, Object... parametros) throws SQLException {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
        }
    }

    public static class DoadorRepository extends RepositorioBase {
        public int cadastrar(String nome, String contato, String tipo,
                             String cpf, String nomeInstituicao, String cnpj) throws SQLException {
            String sql = "INSERT INTO doador (nome, contato, tipo, cpf, nome_instituicao, cnpj) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            return inserir(sql, nome, contato, tipo, cpf, nomeInstituicao, cnpj);
        }

        public List<Map<String, Object>> listar() throws SQLException {
            return consultar("SELECT * FROM doador ORDER BY nome");
        }
    }

    public static class FamiliaRepository extends RepositorioBase {
        public int cadastrar(String nomeResponsavel, String contato, int quantidadePessoas,
                             String nivelUrgencia) throws SQLException {
            return inserir("INSERT INTO familia_acolhida "
                    + "(nome_responsavel, contato, quantidade_pessoas, nivel_urgencia) "
                    + "VALUES (?, ?, ?, ?)",
                    nomeResponsavel, contato, quantidadePessoas, nivelUrgencia);
        }

        public List<Map<String, Object>> listarAtivas() throws SQLException {
            return consultar("SELECT * FROM familia_acolhida WHERE ativo = TRUE "
                    + "ORDER BY FIELD(nivel_urgencia, 'ALTA', 'MEDIA', 'BAIXA'), data_cadastro");
        }

        public int atualizar(int id, String nomeResponsavel, String contato,
                             int quantidadePessoas, String nivelUrgencia) throws SQLException {
            return atualizar("UPDATE familia_acolhida SET nome_responsavel = ?, contato = ?, "
                    + "quantidade_pessoas = ?, nivel_urgencia = ? WHERE id = ?",
                    nomeResponsavel, contato, quantidadePessoas, nivelUrgencia, id);
        }

        public int desativar(int id) throws SQLException {
            return atualizar("UPDATE familia_acolhida SET ativo = FALSE WHERE id = ?", id);
        }
    }

    public static class ProdutoRepository extends RepositorioBase {
        public int cadastrar(String nome, String categoria, String variacao) throws SQLException {
            return inserir("INSERT INTO produto (nome, categoria, variacao) VALUES (?, ?, ?)",
                    nome, categoria, variacao);
        }

        public List<Map<String, Object>> listarAtivos() throws SQLException {
            return consultar("SELECT * FROM produto WHERE ativo = TRUE ORDER BY categoria, nome");
        }
    }

    public static class DoacaoRepository extends RepositorioBase {
        public int registrar(int doadorId, int produtoId, String tipo, int quantidade,
                             java.sql.Date validade, String estadoAptidao,
                             Boolean prontoConsumo, Boolean lacrado, String estacao) throws SQLException {
            String sql = "INSERT INTO doacao (doador_id, produto_id, tipo, quantidade_recebida, "
                    + "validade, estado_aptidao, pronto_consumo, lacrado, estacao) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            return inserir(sql, doadorId, produtoId, tipo, quantidade, validade,
                    estadoAptidao, prontoConsumo, lacrado, estacao);
        }

        public List<Map<String, Object>> listarPorDoador(int doadorId) throws SQLException {
            return consultar("SELECT d.*, p.nome AS produto_nome FROM doacao d "
                    + "JOIN produto p ON p.id = d.produto_id "
                    + "WHERE d.doador_id = ? ORDER BY d.data_cadastro DESC", doadorId);
        }

        public List<Map<String, Object>> listarTodas() throws SQLException {
            return consultar("SELECT d.*, p.nome AS produto_nome, doa.nome AS doador_nome "
                    + "FROM doacao d JOIN produto p ON p.id = d.produto_id "
                    + "JOIN doador doa ON doa.id = d.doador_id ORDER BY d.data_cadastro DESC");
        }
    }

    public static class EstoqueRepository extends RepositorioBase {
        public List<Map<String, Object>> listar() throws SQLException {
            return consultar("SELECT e.id, e.produto_id, p.nome AS produto, p.categoria, "
                    + "e.quantidade_disponivel FROM estoque e "
                    + "JOIN produto p ON p.id = e.produto_id ORDER BY p.nome");
        }

        public int adicionarQuantidade(int produtoId, int quantidade) throws SQLException {
            if (quantidade <= 0) {
                throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
            }
            return atualizar("INSERT INTO estoque (produto_id, quantidade_disponivel) VALUES (?, ?) "
                    + "ON DUPLICATE KEY UPDATE quantidade_disponivel = quantidade_disponivel + VALUES(quantidade_disponivel)",
                    produtoId, quantidade);
        }
    }

    public static class NecessidadeRepository extends RepositorioBase {
        public int cadastrar(int familiaId, int produtoId, int quantidade) throws SQLException {
            return inserir("INSERT INTO necessidade_familia "
                    + "(familia_id, produto_id, quantidade_necessaria) VALUES (?, ?, ?)",
                    familiaId, produtoId, quantidade);
        }

        public List<Map<String, Object>> listarAbertas() throws SQLException {
            return consultar("SELECT n.*, f.nome_responsavel, p.nome AS produto_nome, "
                    + "f.nivel_urgencia FROM necessidade_familia n "
                    + "JOIN familia_acolhida f ON f.id = n.familia_id "
                    + "JOIN produto p ON p.id = n.produto_id "
                    + "WHERE n.status = 'ABERTA' AND f.ativo = TRUE "
                    + "ORDER BY FIELD(f.nivel_urgencia, 'ALTA', 'MEDIA', 'BAIXA'), f.data_cadastro");
        }
    }

    public static class InspecaoRepository extends RepositorioBase {
        public int registrar(int doacaoId, String resultado, String motivo) throws SQLException {
            return inserir("INSERT INTO inspecao_doacao (doacao_id, resultado, motivo) "
                    + "VALUES (?, ?, ?)", doacaoId, resultado, motivo);
        }

        public List<Map<String, Object>> listarPorDoacao(int doacaoId) throws SQLException {
            return consultar("SELECT * FROM inspecao_doacao WHERE doacao_id = ? "
                    + "ORDER BY data_inspecao DESC", doacaoId);
        }
    }

    public static class LiberacaoRepository extends RepositorioBase {
        public int registrar(int doacaoId, String justificativa) throws SQLException {
            return inserir("INSERT INTO liberacao_doacao (doacao_id, justificativa) VALUES (?, ?)",
                    doacaoId, justificativa);
        }

        public List<Map<String, Object>> listar() throws SQLException {
            return consultar("SELECT l.*, d.produto_id, p.nome AS produto_nome "
                    + "FROM liberacao_doacao l JOIN doacao d ON d.id = l.doacao_id "
                    + "JOIN produto p ON p.id = d.produto_id ORDER BY l.data_liberacao DESC");
        }
    }

    public static class DestinacaoRepository extends RepositorioBase {
        public int reservar(int necessidadeId, int doacaoId, int quantidade) throws SQLException {
            if (quantidade <= 0) {
                throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
            }

            try (Connection c = conectar()) {
                c.setAutoCommit(false);
                try {
                    int produtoNecessidade;
                    int pendente;
                    try (PreparedStatement ps = c.prepareStatement(
                            "SELECT produto_id, quantidade_necessaria - quantidade_atendida AS pendente, status "
                                    + "FROM necessidade_familia WHERE id = ? FOR UPDATE")) {
                        ps.setInt(1, necessidadeId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next() || !"ABERTA".equals(rs.getString("status"))) {
                                throw new SQLException("Necessidade não encontrada ou não está aberta.");
                            }
                            produtoNecessidade = rs.getInt("produto_id");
                            pendente = rs.getInt("pendente");
                        }
                    }

                    try (PreparedStatement ps = c.prepareStatement(
                            "SELECT produto_id, estado_aptidao FROM doacao WHERE id = ? FOR UPDATE")) {
                        ps.setInt(1, doacaoId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next() || !"APTA".equals(rs.getString("estado_aptidao"))) {
                                throw new SQLException("Doação não encontrada ou não está apta.");
                            }
                            if (rs.getInt("produto_id") != produtoNecessidade) {
                                throw new SQLException("O produto da doação não corresponde à necessidade.");
                            }
                        }
                    }

                    try (PreparedStatement ps = c.prepareStatement(
                            "SELECT quantidade_disponivel FROM estoque WHERE produto_id = ? FOR UPDATE")) {
                        ps.setInt(1, produtoNecessidade);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next() || rs.getInt("quantidade_disponivel") < quantidade) {
                                throw new SQLException("Estoque insuficiente para realizar a reserva.");
                            }
                        }
                    }

                    if (quantidade > pendente) {
                        throw new SQLException("A quantidade excede o que a família ainda precisa.");
                    }

                    int id;
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO destinacao (necessidade_id, doacao_id, quantidade) "
                                    + "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                        ps.setInt(1, necessidadeId);
                        ps.setInt(2, doacaoId);
                        ps.setInt(3, quantidade);
                        ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (!rs.next()) {
                                throw new SQLException("Não foi possível obter o ID da reserva.");
                            }
                            id = rs.getInt(1);
                        }
                    }

                    try (PreparedStatement ps = c.prepareStatement(
                            "UPDATE estoque SET quantidade_disponivel = quantidade_disponivel - ? "
                                    + "WHERE produto_id = ?")) {
                        ps.setInt(1, quantidade);
                        ps.setInt(2, produtoNecessidade);
                        ps.executeUpdate();
                    }

                    c.commit();
                    return id;
                } catch (SQLException | RuntimeException e) {
                    c.rollback();
                    throw e;
                } finally {
                    c.setAutoCommit(true);
                }
            }
        }

        public int confirmarEntrega(int destinacaoId) throws SQLException {
            return atualizar("UPDATE destinacao SET status = 'CONCLUIDA', "
                    + "data_conclusao = CURRENT_TIMESTAMP "
                    + "WHERE id = ? AND status = 'RESERVADA'", destinacaoId);
        }

        public List<Map<String, Object>> listarHistorico() throws SQLException {
            return consultar("SELECT dt.*, f.nome_responsavel, p.nome AS produto_nome "
                    + "FROM destinacao dt "
                    + "JOIN necessidade_familia n ON n.id = dt.necessidade_id "
                    + "JOIN familia_acolhida f ON f.id = n.familia_id "
                    + "JOIN doacao d ON d.id = dt.doacao_id "
                    + "JOIN produto p ON p.id = d.produto_id "
                    + "ORDER BY dt.data_reserva DESC");
        }
    }
}
