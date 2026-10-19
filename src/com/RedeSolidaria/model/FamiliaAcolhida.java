package com.RedeSolidaria.model.enums;

import com.redesolidaria.model.enums.NivelUrgencia;
import java.time.LocalDateTime;

public class FamiliaAcolhida {
    private int id;
    private String nomeResponsavel;
    private String contato;
    private int quantidadePessoas;
    private NivelUrgencia nivelUrgencia;
    private LocalDateTime dataCadastro;
    private boolean ativo;
}