package com.RedeSolidaria.model;

public class DoacaoRoupa extends Doacao {

    private String estacao;

    public DoacaoRoupa(int id, Doador doador, Produto produto, int quantidade, String estacao) {
        super(id, doador, produto, quantidade, null); // roupa não tem validade
        this.estacao = estacao;
    }

    @Override
    public int calcularPrioridade() {
        if ("Inverno".equalsIgnoreCase(estacao) || "Outono".equalsIgnoreCase(estacao)) {
            return 2;
        }
        return 3;
    }

    public String getEstacao() {
        return estacao;
    }

    public void setEstacao(String estacao) {
        this.estacao = estacao;
    }
}