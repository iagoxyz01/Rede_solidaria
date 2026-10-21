package com.RedeSolidaria.model;

import com.RedeSolidaria.model.enums.CategoriaProduto;

public class Produto {
    private int id;
    private String nome;
    private CategoriaProduto categoria;
    private String variacao;
    private boolean ativo;

    public Produto(int id, String nome, CategoriaProduto categoria, String variacao) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.variacao = variacao;
        this.ativo = true;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public CategoriaProduto getCategoria() { return categoria; }
    public String getVariacao() { return variacao; }
    public boolean isAtivo() { return ativo; }
}