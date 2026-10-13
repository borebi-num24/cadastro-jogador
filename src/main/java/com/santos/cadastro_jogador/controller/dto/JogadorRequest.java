package com.santos.cadastro_jogador.business.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.santos.cadastro_jogador.infraestructure.entitys.Posicao;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record JogadorRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotNull(message = "A posição é obrigatória")
        Posicao posicao,

        @JsonAlias({"numero_camisa", "camisa"})
        @NotNull(message = "O número da camisa é obrigatório")
        @Min(value = 1, message = "O número da camisa deve estar entre 1 e 99")
        @Max(value = 99, message = "O número da camisa deve estar entre 1 e 99")
        Integer numeroCamisa,

        @JsonAlias({"data_nascimento"})
        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @Size(max = 60, message = "A nacionalidade deve ter no máximo 60 caracteres")
        String nacionalidade
) {
}
