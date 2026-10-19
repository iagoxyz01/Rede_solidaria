
package com.RedeSolidaria.model;

public class DoacaoRoupa extends Doacao {

    private String tamanho;
    private String tipoEstacao;

    public DoacaoRoupa(int id, String descricao, int quantidade, String tamanho, String tipoEstacao) {
        super(id, descricao, quantidade);
        this.tamanho = tamanho;
        this.tipoEstacao = tipoEstacao;
    }

    @Override
    public int calcularPrioridade() {
        if ("Inverno".equalsIgnoreCase(tipoEstacao)) {
            return 2;
        } else {
            return 1;
        }
    }

    public String getTamanho() {
        return tamanho;
    }

    public String getTipoEstacao() {
        return tipoEstacao;
    }
}
