
package com.RedeSolidaria.model;

import com.RedeSolidaria.model.enums.ResultadoInspecao;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Doacao {

    private int id;
    private Doador doador;
    private Produto produto;
    private int quantidadeRecebida;
    private LocalDate validade;
    private LocalDateTime dataCadastro;
    private ResultadoInspecao estadoAptidao;

    public Doacao(
            int id,
            Doador doador,
            Produto produto,
            int quantidadeRecebida,
            LocalDate validade) {

        this.id = id;
        this.doador = doador;
        this.produto = produto;
        this.quantidadeRecebida = quantidadeRecebida;
        this.validade = validade;
        this.dataCadastro = LocalDateTime.now();
        this.estadoAptidao = ResultadoInspecao.BLOQUEADA;
    }

    public abstract int calcularPrioridade();

    public int getId() {
        return id;
    }

    public Doador getDoador() {
        return doador;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidadeRecebida() {
        return quantidadeRecebida;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public ResultadoInspecao getEstadoAptidao() {
        return estadoAptidao;
    }

    public boolean verificarAptidao() {
        return estadoAptidao == ResultadoInspecao.APTA;
    }

public void atualizarAptidao(ResultadoInspecao resultado) {
    if (resultado == null) {
        throw new IllegalArgumentException("O resultado da inspeção é obrigatório");
    }

    this.estadoAptidao = resultado;
}
}
