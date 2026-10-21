
package com.RedeSolidaria.model;

import java.time.LocalDateTime;

public class LiberacaoDoacao {

    private int id;
    private Doacao doacao;
    private LocalDateTime dataLiberacao;
    private String justificativa;

    public LiberacaoDoacao(int id, Doacao doacao, String justificativa) {
        this.id = id;
        this.doacao = doacao;
        this.dataLiberacao = LocalDateTime.now();
        this.justificativa = justificativa;
    }

    public int getId() {
        return id;
    }

    public Doacao getDoacao() {
        return doacao;
    }

    public LocalDateTime getDataLiberacao() {
        return dataLiberacao;
    }

    public String getJustificativa() {
        return justificativa;
    }
}
