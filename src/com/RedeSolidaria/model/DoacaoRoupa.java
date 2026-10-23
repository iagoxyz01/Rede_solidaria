package com.RedeSolidaria.model;

public class DoacaoRoupa extends Doacao {
    private String estacao;

    public DoacaoRoupa(int id, String descricao, String estacao) {
        super(id, descricao);
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
