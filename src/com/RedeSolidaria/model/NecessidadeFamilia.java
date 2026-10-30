
package com.RedeSolidaria.model;

import com.RedeSolidaria.model.enums.StatusNecessidade;

public class NecessidadeFamilia {

    private int id;
    private FamiliaAcolhida familia;
    private Produto produto;
    private int quantidadeNecessaria;
    private int quantidadeAtendida;
    private StatusNecessidade status;

    public NecessidadeFamilia(
            int id,
            FamiliaAcolhida familia,
            Produto produto,
            int quantidadeNecessaria) {

        this.id = id;
        this.familia = familia;
        this.produto = produto;
        this.quantidadeNecessaria = quantidadeNecessaria;
        this.quantidadeAtendida = 0;
        this.status = StatusNecessidade.ABERTA;
    }

    public int getId() {
        return id;
    }

    public FamiliaAcolhida getFamilia() {
        return familia;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidadeNecessaria() {
        return quantidadeNecessaria;
    }

    public int getQuantidadeAtendida() {
        return quantidadeAtendida;
    }

    public StatusNecessidade getStatus() {
        return status;
    }

    public int calcularQuantidadePendente() {
        return quantidadeNecessaria - quantidadeAtendida;
    }

    public void registrarAtendimento(int quantidade){
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero"
            );
        }
        if (quantidade > calcularQuantidadePendente()){
            throw new IllegalArgumentException(
                "A quantiadde informada excede o necessário"
            );
        }
        
        quantidadeAtendida += quantidade;

        if (calcularQuantidadePendente() == 0){
            status = StatusNecessidade.CONCLUIDA;
        }

    }
    public void encerrar() {
        status = StatusNecessidade.ENCERRADA;
    }

}

