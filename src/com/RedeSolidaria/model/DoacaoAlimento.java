package com.RedeSolidaria.model;
import java.time.LocalDate;
public class DoacaoAlimento extends Doacao {
 private boolean prontoConsumo;
 public DoacaoAlimento(int id,Doador doador,Produto produto,int quantidade,LocalDate validade,boolean prontoConsumo){super(id,doador,produto,quantidade,validade);this.prontoConsumo=prontoConsumo;}
 public boolean isProntoConsumo(){return prontoConsumo;}
 @Override public int calcularPrioridade(){return prontoConsumo?3:2;}
}