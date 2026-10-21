package com.RedeSolidaria.model;

import com.RedeSolidaria.model.enums.NivelUrgencia;
import java.time.LocalDateTime;

public class FamiliaAcolhida {
    private int id;
    private String nomeResponsavel;
    private String contato;
    private int quantidadePessoas;
    private NivelUrgencia nivelUrgencia;
    private LocalDateTime dataCadastro;
    private boolean ativo;

    public FamiliaAcolhida(int id, String nomeResponsavel, String contato, int quantidadePessoas, NivelUrgencia nivelUrgencia) {
        this.id = id;
        this.nomeResponsavel = nomeResponsavel;
        this.contato = contato;
        this.quantidadePessoas = quantidadePessoas;
        this.nivelUrgencia = nivelUrgencia;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }

    public int getId() { return id; }
    public String getNomeResponsavel() { return nomeResponsavel; }
    public String getContato() { return contato; }
    public int getQuantidadePessoas() { return quantidadePessoas; }
    public NivelUrgencia getNivelUrgencia() { return nivelUrgencia; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public boolean isAtivo() { return ativo; }
}