package com.RedeSolidaria.model;

public class Cidadao extends Doador{
    private String cpf;

    public Cidadao(int id, String nome, String contato, String cpf) {
        super(id, nome, contato);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    
    

}