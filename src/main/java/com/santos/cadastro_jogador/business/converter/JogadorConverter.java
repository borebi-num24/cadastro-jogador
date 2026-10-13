package com.santos.cadastro_jogador.business.converter;

import com.santos.cadastro_jogador.business.dto.JogadorPatchRequest;
import com.santos.cadastro_jogador.business.dto.JogadorRequest;
import com.santos.cadastro_jogador.infraestructure.entitys.Jogador;
import org.springframework.stereotype.Component;

@Component
public class JogadorConverter {

    public Jogador paraEntidade(JogadorRequest dados) {
        return Jogador.builder()
                .nome(dados.nome().trim())
                .posicao(dados.posicao())
                .numeroCamisa(dados.numeroCamisa())
                .dataNascimento(dados.dataNascimento())
                .nacionalidade(dados.nacionalidade())
                .build();
    }

    /**
     * Técnica do vídeo: para cada campo, se o cliente enviou (diferente de null) usa o novo valor;
     * senão, mantém o que já está gravado. Assim não "apagamos" dados sem querer.
     */
    public Jogador mesclar(Jogador atual, JogadorPatchRequest dados) {
        return Jogador.builder()
                .id(atual.getId()) // o id sempre vem da entidade que já existe no banco
                .nome(dados.nome() != null ? dados.nome().trim() : atual.getNome())
                .posicao(dados.posicao() != null ? dados.posicao() : atual.getPosicao())
                .numeroCamisa(dados.numeroCamisa() != null ? dados.numeroCamisa() : atual.getNumeroCamisa())
                .dataNascimento(dados.dataNascimento() != null ? dados.dataNascimento() : atual.getDataNascimento())
                .nacionalidade(dados.nacionalidade() != null ? dados.nacionalidade() : atual.getNacionalidade())
                .build();
    }
}
