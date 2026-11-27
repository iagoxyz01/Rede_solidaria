
package com.RedeSolidaria.model;

import com.RedeSolidaria.model.enums.ResultadoInspecao;
import java.time.LocalDateTime;

public class InspecaoDoacao {

    private int id;
    private Doacao doacao;
    private LocalDateTime dataInspecao;
    private ResultadoInspecao resultado;
    private String motivo;

    public InspecaoDoacao(
            int id,
            Doacao doacao,
            ResultadoInspecao resultado,
            String motivo) {

        if (doacao == null || resultado == null) {
            throw new IllegalArgumentException(
                "A doação e o resultado são obrigatórios"
            );
        }

        this.id = id;
        this.doacao = doacao;
        this.dataInspecao = LocalDateTime.now();
        this.resultado = resultado;
        this.motivo = motivo;

        this.doacao.atualizarAptidao(resultado);
    }

    public int getId() {
        return id;
    }

    public Doacao getDoacao() {
        return doacao;
    }

    public LocalDateTime getDataInspecao() {
        return dataInspecao;
    }

    public ResultadoInspecao getResultado() {
        return resultado;
    }

    public String getMotivo() {
        return motivo;
    }

    public boolean foiAprovada() {
        return resultado == ResultadoInspecao.APTA;
    }
}
