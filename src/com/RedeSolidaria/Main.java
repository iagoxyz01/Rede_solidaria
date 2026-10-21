package com.RedeSolidaria;

import com.RedeSolidaria.model.FamiliaAcolhida;
import com.RedeSolidaria.model.enums.NivelUrgencia;
import com.RedeSolidaria.services.ONGCentral;

public class Main {

    public static void main(String[] args) {

        ONGCentral ong = new ONGCentral();

        ong.cadastrarFamilia(
                new FamiliaAcolhida(
                        1,
                        "Família de exemplo",
                        "contato",
                        4,
                        NivelUrgencia.MEDIA
                )
        );

        System.out.println("Rede Solidária - estrutura inicial");
        System.out.println("Famílias cadastradas: " + ong.getFamilias().size());
    }
}
