package com.RedeSolidaria;

import com.RedeSolidaria.model.FamiliaAcolhida;
import com.RedeSolidaria.model.enums.NivelUrgencia;
import com.RedeSolidaria.services.ONGCentral;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        ONGCentral ong = new ONGCentral();

        System.out.println("=== Rede Solidária ===");

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ", 0, 3);

            switch (opcao) {
                case 1 -> cadastrarFamilia(ong);
                case 2 -> listarFamilias("Famílias cadastradas", ong.getFamilias());
                case 3 -> listarFamilias("Famílias por prioridade de atendimento", ong.priorizarFamilias());
                case 0 -> System.out.println("Encerrando o sistema. Até logo!");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("--- Menu ---");
        System.out.println("1 - Cadastrar família acolhida");
        System.out.println("2 - Listar famílias cadastradas");
        System.out.println("3 - Listar famílias por prioridade");
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

    // ---------- Métodos auxiliares de leitura ----------

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
}