package com.RedeSolidaria.services;
import com.RedeSolidaria.model.Doacao;
import com.RedeSolidaria.model.FamiliaAcolhida;
import java.util.ArrayList;
import java.util.List;
/** Serviço inicial; regras completas serão implementadas em etapas futuras. */
public class ONGCentral {
 private final List<FamiliaAcolhida> familias=new ArrayList<>();
 private final List<Doacao> doacoes=new ArrayList<>();
 public void cadastrarFamilia(FamiliaAcolhida familia){familias.add(familia);}
 public void registrarDoacao(Doacao doacao){doacoes.add(doacao);}
 public List<FamiliaAcolhida> getFamilias(){return new ArrayList<>(familias);}
 public List<Doacao> getDoacoes(){return new ArrayList<>(doacoes);}
 public void priorizarFamilias(){/* Etapa futura. */}
 public void realizarReserva(){/* Etapa futura. */}
 public void confirmarEntrega(){/* Etapa futura. */}
}