package com.RedeSolidaria.model;

public class DoacaoHigiene extends Doacao {

    private boolean isLacrado;

    public DoacaoHigiene(int id, String descricao, int quantidade, boolean isLacrado) {
        super(id, descricao, quantidade);
        this.isLacrado = isLacrado;
    }

    @Override
    public int calcularPrioridade() {
        if (isLacrado) {
            return 2;
        } else {
            return 1;
        }
    }

    public boolean isLacrado() {
        return isLacrado;
    }
}