package com.RedeSolidaria.model;

import com.RedeSolidaria.model.enums.EstadoAptidao;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Doacao {
    private int id;
    private Doador doador;
    private Produto produto;
    private int quantidadeRecebida;
    private LocalDate validade;
    private LocalDateTime dataCadastro;
    private EstadoAptidao estadoAptidao;

    public Doacao(int id, Doador doador, Produto produto, int quantidadeRecebida, LocalDate validade) {
        this.id = id;
        this.doador = doador;
        this.produto = produto;
        this.quantidadeRecebida = quantidadeRecebida;
        this.validade = validade;
        this.dataCadastro = LocalDateTime.now();
        this.estadoAptidao = EstadoAptidao.APTA;
    }

    public abstract int calcularPrioridade();
    public int getId() { return id; }
    public Doador getDoador() { return doador; }
    public Produto getProduto() { return produto; }
    public int getQuantidadeRecebida() { return quantidadeRecebida; }
    public LocalDate getValidade() { return validade; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public EstadoAptidao getEstadoAptidao() { return estadoAptidao; }
    public boolean verificarAptidao() { return estadoAptidao == EstadoAptidao.APTA; }
}