package com.RedeSolidaria.model;

import com.RedeSolidaria.model.enums.EstadoAptidao;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Doacao {
    private int id;
    private int quantidadeRecebida;
        private LocalDate validade;
        private LocalDateTime dataCadastro;
    private EstadoAptidao estadoAptidao;

    public abstract int calcularPrioridade();

public boolean verificarAptidao() {
    return this.estadoAptidao == EstadoAptidao.APTA;
}

public int getId() {
    return id;
}
public EstadoAptidao getEstadoAptidao() {
    return estadoAptidao;
}
}