package com.RedeSolidaria.model;

public class Estoque {
    private final int id;
    private final Produto produto;
    private int quantidadeDisponivel;

    public Estoque(int id, Produto produto, int quantidadeDisponivel) {
        this.id = id;
        this.produto = produto;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }
    
    public void reservarQuantidade(int quantidade) {
    if (quantidade <= 0) {
        throw new IllegalArgumentException(
            "A quantidade deve ser maior que zero"
        );
    }

    if (quantidade > quantidadeDisponivel) {
        throw new IllegalArgumentException(
            "Estoque insuficiente"
        );
    }
    quantidadeDisponivel -= quantidade;
}

    public int getId() { return id; }
    public Produto getProduto() { return produto; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
}