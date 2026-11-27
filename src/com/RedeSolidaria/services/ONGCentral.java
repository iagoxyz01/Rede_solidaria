
package com.RedeSolidaria.services;

import com.RedeSolidaria.model.Destinacao;
import com.RedeSolidaria.model.Doacao;
import com.RedeSolidaria.model.Estoque;
import com.RedeSolidaria.model.FamiliaAcolhida;
import com.RedeSolidaria.model.NecessidadeFamilia;
import com.RedeSolidaria.model.enums.NivelUrgencia;
import com.RedeSolidaria.model.enums.StatusNecessidade;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ONGCentral {

    private final List<FamiliaAcolhida> familias = new ArrayList<>();
    private final List<Doacao> doacoes = new ArrayList<>();
    private final List<Destinacao> destinacoes = new ArrayList<>();

    public List<FamiliaAcolhida> priorizarFamilias() {
        List<FamiliaAcolhida> prioritarias = new ArrayList<>();

        for (FamiliaAcolhida familia : familias) {
            if (familia.isAtivo()) {
                prioritarias.add(familia);
            }
        }

        prioritarias.sort(
            Comparator
                .comparingInt(this::pontuacaoUrgencia)
                .reversed()
                .thenComparing(FamiliaAcolhida::getDataCadastro)
        );

        return prioritarias;
    }

    public void cadastrarFamilia(FamiliaAcolhida familia) {
        familias.add(familia);
    }

    private int pontuacaoUrgencia(FamiliaAcolhida familia) {
        NivelUrgencia nivel = familia.getNivelUrgencia();

        if (nivel == NivelUrgencia.ALTA) {
            return 3;
        }

        if (nivel == NivelUrgencia.MEDIA) {
            return 2;
        }

        return 1;
    }

    public void registrarDoacao(Doacao doacao) {
        if (doacao == null) {
            throw new IllegalArgumentException("A doação é obrigatória");
        }

        doacoes.add(doacao);
    }

    public List<FamiliaAcolhida> getFamilias() {
        return new ArrayList<>(familias);
    }

    public List<Doacao> getDoacoes() {
        return new ArrayList<>(doacoes);
    }

    public List<Destinacao> getDestinacoes() {
        return new ArrayList<>(destinacoes);
    }

    public Destinacao realizarReserva(
            int idDestinacao,
            NecessidadeFamilia necessidade,
            Doacao doacao,
            Estoque estoque,
            int quantidade) {

        if (necessidade == null || doacao == null || estoque == null) {
            throw new IllegalArgumentException(
                "Os dados da reserva são obrigatórios"
            );
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero"
            );
        }

        if (!necessidade.getFamilia().isAtivo()) {
            throw new IllegalArgumentException("A família está inativa");
        }

        if (necessidade.getStatus() != StatusNecessidade.ABERTA) {
            throw new IllegalArgumentException("A necessidade não está aberta");
        }

        if (!doacao.verificarAptidao()) {
            throw new IllegalArgumentException("A doação não está apta");
        }

        if (necessidade.getProduto().getId() != estoque.getProduto().getId()
                || doacao.getProduto().getId() != estoque.getProduto().getId()) {
            throw new IllegalArgumentException(
                "O produto do estoque não corresponde à doação ou à necessidade"
            );
        }

        if (quantidade > necessidade.calcularQuantidadePendente()) {
            throw new IllegalArgumentException(
                "A quantidade excede a necessidade da família"
            );
        }

        if (quantidade > doacao.getQuantidadeRecebida()) {
            throw new IllegalArgumentException(
                "Quantidade maior que a recebida na doação"
            );
        }

        estoque.reservarQuantidade(quantidade);
        necessidade.registrarAtendimento(quantidade);

        Destinacao destinacao = new Destinacao(
            idDestinacao,
            necessidade,
            doacao,
            quantidade
        );

        destinacoes.add(destinacao);

        return destinacao;
    }

    public void confirmarEntrega(Destinacao destinacao) {
        if (destinacao == null) {
            throw new IllegalArgumentException("A destinação é obrigatória");
        }

        if (!destinacoes.contains(destinacao)) {
            throw new IllegalArgumentException("Destinação não encontrada");
        }

        destinacao.concluir();
    }

    public void cancelarReserva(
            Destinacao destinacao,
            Estoque estoque,
            String motivo) {

        if (destinacao == null || estoque == null) {
            throw new IllegalArgumentException(
                "Dados obrigatórios não informados"
            );
        }

        if (!destinacoes.contains(destinacao)) {
            throw new IllegalArgumentException("Destinação não encontrada");
        }

        destinacao.cancelar(motivo);

        destinacao.getNecessidade().cancelarAtendimento(
            destinacao.getQuantidade()
        );

        estoque.devolverQuantidade(destinacao.getQuantidade());
    }
}
