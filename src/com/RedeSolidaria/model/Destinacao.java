package com.RedeSolidaria.model;
import com.RedeSolidaria.model.enums.StatusDestinacao;
import java.time.LocalDateTime;
public class Destinacao {
 private int id; private NecessidadeFamilia necessidade; private Doacao doacao; private int quantidade; private LocalDateTime dataReserva; private LocalDateTime dataConclusao; private LocalDateTime dataCancelamento; private String motivoCancelamento; private StatusDestinacao status;
 public Destinacao(int id,NecessidadeFamilia necessidade,Doacao doacao,int quantidade){this.id=id;this.necessidade=necessidade;this.doacao=doacao;this.quantidade=quantidade;this.dataReserva=LocalDateTime.now();this.status=StatusDestinacao.RESERVADA;}
 public int getId(){return id;} public NecessidadeFamilia getNecessidade(){return necessidade;} public Doacao getDoacao(){return doacao;} public int getQuantidade(){return quantidade;} public LocalDateTime getDataReserva(){return dataReserva;} public LocalDateTime getDataConclusao(){return dataConclusao;} public LocalDateTime getDataCancelamento(){return dataCancelamento;} public String getMotivoCancelamento(){return motivoCancelamento;} public StatusDestinacao getStatus(){return status;}
}