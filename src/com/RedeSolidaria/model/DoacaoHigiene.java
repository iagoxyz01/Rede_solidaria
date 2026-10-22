package com.RedeSolidaria.model;

import java.time.LocalDate;

public class DoacaoHigiene extends Doacao {
    private boolean lacrado;

    public DoacaoHigiene(int id, Doador doador, Produto produto, int quantidade, LocalDate validade, boolean lacrado) {
        super(id, doador, produto, quantidade, validade);
        this.lacrado = lacrado;
    }

    public boolean isLacrado() { return lacrado; }
    @Override public int calcularPrioridade() { return lacrado ? 2 : 1; }
}