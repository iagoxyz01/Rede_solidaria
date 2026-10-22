package com.RedeSolidaria.model;

public class Estoque {
    private int id;
    private Produto produto;
    private int quantidadeDisponivel;

    public Estoque(int id, Produto produto, int quantidadeDisponivel) {
        this.id = id;
        this.produto = produto;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public int getId() { return id; }
    public Produto getProduto() { return produto; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
}