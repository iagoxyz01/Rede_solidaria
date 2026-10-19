package com.RedeSolidaria.model.enums;

public abstract class Doador {
    private int id;
    private String nome;
    private String contato;

    public Doador(int id, String nome, String contato) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getContato() {
        return contato;
    }
}
