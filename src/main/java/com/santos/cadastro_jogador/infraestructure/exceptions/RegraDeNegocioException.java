package com.santos.cadastro_jogador.infraestructure.exceptions;

public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
