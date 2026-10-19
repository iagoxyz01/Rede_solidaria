package com.RedeSolidaria.model.enums;

public class DoacaoAlimento extends Doacao {
    private boolean prontoConsumo;

    @Override
    public int calcularPrioridade() {
        return 10; 
    }
}