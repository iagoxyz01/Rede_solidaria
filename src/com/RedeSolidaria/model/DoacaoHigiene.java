package com.RedeSolidaria.model;
import java.time.LocalDate;

public class DoacaoHigiene extends Doacao {
    private boolean lacrado;

    public DoacaoHigiene(
            int id,
            Doador doador,
            Produto produto,
            int quantidadeRecebida,
            LocalDate validade,
            boolean lacrado) {

        super(id, doador, produto, quantidadeRecebida, validade);
        this.lacrado = lacrado;
    }

    @Override
    public int calcularPrioridade() {
        // Produtos lacrados garantem saude preventiva
        return lacrado ? 3 : 4;
    }
}
