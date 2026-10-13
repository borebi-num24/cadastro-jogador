package com.santos.cadastro_jogador;

import com.santos.cadastro_jogador.infraestructure.entitys.Jogador;
import com.santos.cadastro_jogador.infraestructure.entitys.Posicao;
import com.santos.cadastro_jogador.infraestructure.repository.JogadorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class CadastroJogadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroJogadorApplication.class, args);
	}

	/**
	 * CommandLineRunner: roda automaticamente quando a aplicação sobe.
	 * Popula o banco com dados de exemplo (só se estiver vazio).
	 */
	@Bean
	CommandLineRunner carregarDadosIniciais(JogadorRepository repository) {
		return args -> {
			if (repository.count() > 0) {
				return;
			}
			var jogadores = List.of(
					new Jogador(null, "Pelé", Posicao.ATACANTE, 10, LocalDate.of(1940, 10, 23), "Brasileira"),
					new Jogador(null, "Neymar", Posicao.ATACANTE, 11, LocalDate.of(1992, 2, 5), "Brasileira"),
					new Jogador(null, "Robinho", Posicao.ATACANTE, 7, LocalDate.of(1984, 1, 25), "Brasileira"),
					new Jogador(null, "Diego Ribas", Posicao.MEIA, 8, LocalDate.of(1985, 2, 28), "Brasileira")
			);
			repository.saveAll(jogadores);
			System.out.println(">>> " + jogadores.size() + " jogadores de exemplo cadastrados.");
		};
	}

}
