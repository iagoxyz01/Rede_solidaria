package com.RedeSolidaria.model;

public class DoacaoHigiene extends Doacao {
    private boolean lacrado;

    public DoacaoHigiene(int id, String descricao, boolean lacrado) {
        super(id, descricao);
        this.lacrado = lacrado;
    }

    @Override
    public int calcularPrioridade() {
        // Produtos lacrados garantem saude preventiva
        return lacrado ? 3 : 4;
    }
