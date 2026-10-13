package com.santos.cadastro_jogador.business.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.santos.cadastro_jogador.infraestructure.entitys.Posicao;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Atualização parcial: todos os campos são opcionais.
 * Campo ausente (null) = "não mexe", o valor atual do banco é mantido.
 * As validações só rodam nos campos que foram enviados.
 */
public record JogadorPatchRequest(

        @Pattern(regexp = ".*\\S.*", message = "O nome não pode ser vazio")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        Posicao posicao,

        @JsonAlias({"numero_camisa", "camisa"})
        @Min(value = 1, message = "O número da camisa deve estar entre 1 e 99")
        @Max(value = 99, message = "O número da camisa deve estar entre 1 e 99")
        Integer numeroCamisa,

        @JsonAlias({"data_nascimento"})
        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @Size(max = 60, message = "A nacionalidade deve ter no máximo 60 caracteres")
        String nacionalidade
) {
}
