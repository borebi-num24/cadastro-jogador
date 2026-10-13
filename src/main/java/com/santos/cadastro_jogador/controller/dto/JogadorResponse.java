package com.santos.cadastro_jogador.business.dto;

import com.santos.cadastro_jogador.infraestructure.entitys.Jogador;
import com.santos.cadastro_jogador.infraestructure.entitys.Posicao;

import java.time.LocalDate;

public record JogadorResponse(
        Integer id,
        String nome,
        Posicao posicao,
        Integer numeroCamisa,
        LocalDate dataNascimento,
        String nacionalidade
) {
    public static JogadorResponse de(Jogador jogador) {
        return new JogadorResponse(
                jogador.getId(),
                jogador.getNome(),
                jogador.getPosicao(),
                jogador.getNumeroCamisa(),
                jogador.getDataNascimento(),
                jogador.getNacionalidade());
    }
}
