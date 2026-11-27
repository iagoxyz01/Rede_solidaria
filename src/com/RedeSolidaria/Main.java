package com.RedeSolidaria;

import com.RedeSolidaria.model.FamiliaAcolhida;
import com.RedeSolidaria.model.enums.NivelUrgencia;
import com.RedeSolidaria.services.DoadorService;
import com.RedeSolidaria.services.ONGCentral;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DoadorService doadorService = new DoadorService();

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            scanner = input;
            ONGCentral ong = new ONGCentral();

            System.out.println("=== Rede Solidária ===");

            int opcao;
            do {
                exibirMenu();
                opcao = lerInteiro("Escolha uma opção: ", 0, 4);

                switch (opcao) {
                    case 1 -> cadastrarFamilia(ong);
                    case 2 -> listarFamilias("Famílias cadastradas", ong.getFamilias());
                    case 3 -> listarFamilias("Famílias por prioridade de atendimento", ong.priorizarFamilias());
                    case 4 -> menuAreaDoador();
                    case 0 -> System.out.println("Encerrando o sistema. Até logo!");
                }
            } while (opcao != 0);
        }
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("--- Menu Principal ---");
        System.out.println("1 - Cadastrar família acolhida");
        System.out.println("2 - Listar famílias cadastradas");
        System.out.println("3 - Listar famílias por prioridade");
        System.out.println("4 - Área do doador");
        System.out.println("0 - Sair");
    }

    private static void cadastrarFamilia(ONGCentral ong) {
        System.out.println();
        System.out.println("--- Novo cadastro ---");

        int id = ong.getFamilias().size() + 1;
        String nome = lerTexto("Nome do responsável: ");
        String contato = lerTexto("Contato: ");
        int quantidade = lerInteiro("Quantidade de pessoas: ", 1, 50);
        NivelUrgencia urgencia = lerUrgencia();

        ong.cadastrarFamilia(new FamiliaAcolhida(id, nome, contato, quantidade, urgencia));

        System.out.println("Família cadastrada com sucesso! (ID " + id + ")");
    }

    private static void listarFamilias(String titulo, List<FamiliaAcolhida> familias) {
        System.out.println();
        System.out.println("--- " + titulo + ": " + familias.size() + " ---");

        if (familias.isEmpty()) {
            System.out.println("Nenhuma família encontrada.");
            return;
        }

        for (FamiliaAcolhida f : familias) {
            System.out.printf("ID %d | %s | Contato: %s | Pessoas: %d | Urgência: %s | Cadastro: %s%n",
                    f.getId(),
                    f.getNomeResponsavel(),
                    f.getContato(),
                    f.getQuantidadePessoas(),
                    f.getNivelUrgencia(),
                    f.getDataCadastro().format(FORMATO_DATA));
        }
    }

    private static void menuAreaDoador() {
        int opcao;
        do {
            System.out.println();
            System.out.println("=== ÁREA DO DOADOR ===");
            System.out.println("1 - Cadastrar doador");
            System.out.println("2 - Consultar necessidades disponíveis");
            System.out.println("3 - Registrar doação");
            System.out.println("4 - Histórico e acompanhamento de status");
            System.out.println("5 - Consultar cadastro de doador por ID");
            System.out.println("0 - Voltar ao menu principal");

            opcao = lerInteiro("Escolha uma opção: ", 0, 5);

            try {
                switch (opcao) {
                    case 1 -> cadastrarDoador();
                    case 2 -> consultarNecessidades();
                    case 3 -> registrarDoacao();
                    case 4 -> consultarHistorico();
                    case 5 -> consultarDoadorPorId();
                    case 0 -> System.out.println("Voltando ao menu principal...");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
                System.out.println("Confira se o MySQL está ligado, se o banco foi criado, "
                        + "se o Connector/J está configurado e se as credenciais estão corretas.");
            } catch (IllegalArgumentException e) {
                System.out.println("Dados inválidos: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private static void cadastrarDoador() throws SQLException {
        System.out.println("\n--- CADASTRO DE DOADOR ---");
        String nome = lerTexto("Nome do doador/responsável: ");
        String contato = lerTexto("Contato (telefone ou e-mail): ");
        System.out.println("Tipo: 1 - Cidadão | 2 - Organização");
        int tipoOpcao = lerInteiro("Escolha: ", 1, 2);

        String tipo = tipoOpcao == 1 ? "CIDADAO" : "ORGANIZACAO";
        String cpf = null;
        String instituicao = null;
        String cnpj = null;

        if (tipoOpcao == 1) {
            cpf = lerTexto("CPF: ");
        } else {
            instituicao = lerTexto("Nome da instituição: ");
            cnpj = lerTexto("CNPJ: ");
        }

        int id = doadorService.cadastrarDoador(nome, contato, tipo, cpf, instituicao, cnpj);
        System.out.println("Doador cadastrado com sucesso! ID: " + id);
        System.out.println("Guarde esse ID: ele será usado para registrar doações.");
    }

    private static void consultarNecessidades() throws SQLException {
        System.out.println("\n--- NECESSIDADES DISPONÍVEIS ---");
        List<String> necessidades = doadorService.listarNecessidades();
        if (necessidades.isEmpty()) {
            System.out.println("Não existem necessidades abertas no momento.");
            return;
        }
        necessidades.forEach(System.out::println);
    }

    private static void registrarDoacao() throws SQLException {
        System.out.println("\n--- REGISTRAR DOAÇÃO ---");
        int doadorId = lerInteiro("ID do doador: ", 1, Integer.MAX_VALUE);
        String nomeProduto = lerTexto("Nome do produto: ");

        System.out.println("Categoria: 1 - ALIMENTO | 2 - ROUPA | 3 - HIGIENE");
        int categoriaOpcao = lerInteiro("Escolha: ", 1, 3);
        String categoria = categoriaOpcao == 1 ? "ALIMENTO"
                : categoriaOpcao == 2 ? "ROUPA" : "HIGIENE";

        String variacao = lerOpcional(
                "Variação (ex.: tamanho, tipo ou marca; Enter para pular): ");
        int quantidade = lerInteiro("Quantidade: ", 1, Integer.MAX_VALUE);
        Date validade = lerValidade();

        Boolean prontoConsumo = null;
        Boolean lacrado = null;
        String estacao = null;

        switch (categoria) {
            case "ALIMENTO" -> {
                prontoConsumo = lerSimNao("É pronto para consumo? (s/n): ");
            }
            case "HIGIENE" -> {
                lacrado = lerSimNao("O produto está lacrado? (s/n): ");
            }
            case "ROUPA" -> {
                estacao = lerOpcional(
                        "Estação (Primavera/Verão/Outono/Inverno; Enter para pular): ");
            }
        }

        int id = doadorService.registrarDoacao(doadorId, nomeProduto, categoria, variacao,
                quantidade, validade, prontoConsumo, lacrado, estacao);
        System.out.println("Doação registrada com sucesso! ID: " + id);
        System.out.println("Situação inicial de aptidão: APTA (conforme regra atual do banco).");
    }

    private static void consultarHistorico() throws SQLException {
        System.out.println("\n--- HISTÓRICO DE DOAÇÕES ---");
        int doadorId = lerInteiro("ID do doador: ", 1, Integer.MAX_VALUE);
        List<String> historico = doadorService.listarHistorico(doadorId);
        if (historico.isEmpty()) {
            System.out.println("Nenhuma doação encontrada para esse doador.");
            return;
        }
        historico.forEach(System.out::println);
    }

    private static void consultarDoadorPorId() throws SQLException {
        System.out.println("\n--- CONSULTAR DOADOR ---");
        int id = lerInteiro("ID do doador: ", 1, Integer.MAX_VALUE);

        String sql = "SELECT id, nome, contato, tipo, cpf, nome_instituicao, cnpj "
                + "FROM doador WHERE id = ?";
        try (var conexao = Conexao.conectar();
             var ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Nome: " + rs.getString("nome"));
                    System.out.println("Contato: " + rs.getString("contato"));
                    System.out.println("Tipo: " + rs.getString("tipo"));
                    System.out.println("CPF: " + rs.getString("cpf"));
                    System.out.println("Instituição: " + rs.getString("nome_instituicao"));
                    System.out.println("CNPJ: " + rs.getString("cnpj"));
                } else {
                    System.out.println("Doador não encontrado.");
                }
            }
        }
    }

    // ---------------- MÉTODOS AUXILIARES ----------------

    private static String lerTexto(String mensagem) {
        String texto;
        do {
            System.out.print(mensagem);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Este campo não pode ficar vazio.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    private static String lerOpcional(String mensagem) {
        System.out.print(mensagem);
        String texto = scanner.nextLine().trim();
        return texto.isEmpty() ? null : texto;
    }

    private static int lerInteiro(String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Digite um número entre " + min + " e " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas números.");
            }
        }
    }

    private static NivelUrgencia lerUrgencia() {
        NivelUrgencia[] niveis = NivelUrgencia.values();

        System.out.println("Nível de urgência:");
        for (int i = 0; i < niveis.length; i++) {
            System.out.println("  " + (i + 1) + " - " + niveis[i]);
        }

        int escolha = lerInteiro("Escolha o nível: ", 1, niveis.length);
        return niveis[escolha - 1];
    }

    private static Date lerValidade() {
        while (true) {
            String valor = lerOpcional(
                    "Validade (AAAA-MM-DD; Enter se não se aplica): ");
            if (valor == null) {
                return null;
            }
            try {
                return Date.valueOf(LocalDate.parse(valor));
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Exemplo: 2027-05-30.");
            }
        }
    }

    private static Boolean lerSimNao(String mensagem) {
        while (true) {
            String valor = lerTexto(mensagem);
            if (valor.equalsIgnoreCase("s")) return true;
            if (valor.equalsIgnoreCase("n")) return false;
            System.out.println("Digite s ou n.");
        }
    }
}
