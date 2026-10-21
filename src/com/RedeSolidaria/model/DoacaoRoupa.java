package com.RedeSolidaria.model;

import java.time.LocalDate;

public class DoacaoRoupa extends Doacao {
    private String tamanho;
    private String tipoEstacao;

    public DoacaoRoupa(int id, Doador doador, Produto produto, int quantidade, LocalDate validade, String tamanho, String tipoEstacao) {
        super(id, doador, produto, quantidade, validade);
        this.tamanho = tamanho;
        this.tipoEstacao = tipoEstacao;
    }

    public String getTamanho() { return tamanho; }
    public String getTipoEstacao() { return tipoEstacao; }
    @Override public int calcularPrioridade() { return "INVERNO".equalsIgnoreCase(tipoEstacao) ? 2 : 1; }
}