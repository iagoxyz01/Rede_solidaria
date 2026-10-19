
package com.RedeSolidaria.model;

import com.RedeSolidaria.model.enums.EstadoAptidao;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Doacao {

    private int id;
    private String descricao;
    private int quantidade;
    private LocalDate validade;
    private LocalDateTime dataCadastro;
    private EstadoAptidao estadoAptidao;

    public Doacao(int id, String descricao, int quantidade) {
        this.id = id;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public abstract int calcularPrioridade();

    public boolean verificarAptidao() {
        return estadoAptidao == EstadoAptidao.APTA;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public EstadoAptidao getEstadoAptidao() {
        return estadoAptidao;
    }
}
