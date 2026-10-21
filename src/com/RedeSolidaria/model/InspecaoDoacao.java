package com.RedeSolidaria.model;
import com.RedeSolidaria.model.enums.ResultadoInspecao;
import java.time.LocalDateTime;
public class InspecaoDoacao {
 private int id; private Doacao doacao; private LocalDateTime dataInspecao; private ResultadoInspecao resultado; private String motivo;
 public InspecaoDoacao(int id,Doacao doacao,ResultadoInspecao resultado,String motivo){this.id=id;this.doacao=doacao;this.dataInspecao=LocalDateTime.now();this.resultado=resultado;this.motivo=motivo;}
 public int getId(){return id;} public Doacao getDoacao(){return doacao;} public LocalDateTime getDataInspecao(){return dataInspecao;} public ResultadoInspecao getResultado(){return resultado;} public String getMotivo(){return motivo;}
}