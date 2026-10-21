package com.RedeSolidaria.model;

public class OrganizacaoDoadora extends Doador {
    private String nomeInstituicao;
    private String cnpj;

    public OrganizacaoDoadora(int id, String nome, String contato, String nomeInstituicao, String cnpj) {
        super(id, nome, contato);
        this.nomeInstituicao = nomeInstituicao;
        this.cnpj = cnpj;
    }

    public String getNomeInstituicao() { return nomeInstituicao; }
    public String getCnpj() { return cnpj; }
}